import React, { useState, useEffect, useRef } from 'react';
import { CircularProgress, Container, Grid, Typography, Modal, Box, IconButton, MenuItem, Select } from '@mui/material';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import PaperListOpinion from '../../components/PaperListOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation, useNavigate } from 'react-router-dom';
import { Network } from 'vis-network/standalone/umd/vis-network.min.js';

function GraphViewOpinion() {
  const [selectedPaper, setSelectedPaper] = useState(null);
  const location = useLocation();
  const [graphPapers, setGraphPapers] = useState([]);
  const [currentView, setCurrentView] = useState('Graph View');
  const [isLoading, setIsLoading] = useState(true);  // Start as loading is true
  const [viewType, setViewType] = useState('');
  const [supporting, setSupporting] = useState({});
  const [opposing, setOpposing] = useState({});
  const [viewMode, setViewMode] = useState('supporting');
  const networkRef = useRef(null);
  const [openModal, setOpenModal] = useState(false);
  const [modalContent, setModalContent] = useState([]);
  const [selectedOpinion, setSelectedOpinion] = useState('');
  const [searchPrompt, setSearchPrompt] = useState('');
  const navigate = useNavigate();

  const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '60%',
    bgcolor: 'background.paper',
    boxShadow: 24,
    borderRadius: '1.5rem',
    p: 4,
    overflow: 'auto',
    maxHeight: '60vh',
  };

  const paperListStyle = {
    maxHeight: '60vh',
    overflowY: 'auto',
    padding: '10px',
  };

  const formatAuthors = (authors) => {
    if (authors && authors.length > 0) {
      return authors[0].name;
    }
    return 'Unknown';
  };

  const capitalizeWords = (str) => {
    return str
      .split(' ')               // cplit the string into an array of words
      .map(word => word.charAt(0).toUpperCase() + word.slice(1)) // capitalize the first letter of each word
      .join(' ');               // join the array back into a string
  };

  const formattedPrompt = capitalizeWords(searchPrompt);
  

  // Refactored effect for fetching and setting state from location
  useEffect(() => {
    const loadData = () => {
      if(location.state?.papers){
        setGraphPapers(location.state.papers);
      }
      // Only set papers, supporting, opposing if present
      if (location.state?.graphPapers) {
        setGraphPapers(location.state.graphPapers);
      }
      if (location.state?.supporting) {
        setSupporting(location.state.supporting);
      }
      if (location.state?.opposing) {
        setOpposing(location.state.opposing);
      }
      setViewType(location.state?.graphViewType);
      if (location.state?.prompt) {
        setSearchPrompt(location.state.prompt);
      }

      // Set loading to false once all data is set
      setIsLoading(false);
    };

    loadData();
  }, [location.state]);

  // Refactored useEffect to depend on isLoading state
  useEffect(() => {
    if (!isLoading && networkRef.current && (Object.keys(supporting).length > 0 || Object.keys(opposing).length > 0)) {
      const nodes = [];
      const edges = [];
      const colors = ['#A3D5A6', '#F28C8C', '#F2C94C', '#56CCF2', '#BB6BD9', '#FF7F50'];

      if (viewMode === 'supporting' || viewMode === 'all') {
        nodes.push({
          id: 'supporting',
          label: 'Supporting',
          color: '#239178',
          shape: 'circle',
          font: { bold: true, color: '#ffffff' },
        });
      }
      if (viewMode === 'opposing' || viewMode === 'all') {
        nodes.push({
          id: 'opposing',
          label: 'Opposing',
          color: '#e53935',
          shape: 'circle',
          font: { bold: true, color: '#ffffff' },
        });
      }

      const breakLabelByWords = (label, maxWordsPerLine) => {
        // replace underscores with spaces
        const cleanLabel = label.replace(/_/g, ' ');
        const words = cleanLabel.split(' ');
        let lines = [];
        for (let i = 0; i < words.length; i += maxWordsPerLine) {
          lines.push(words.slice(i, i + maxWordsPerLine).join(' '));
        }
        return lines.join('\n');
      };

      if (viewMode === 'supporting' || viewMode === 'all') {
        Object.keys(supporting).forEach((opinion, index) => {
          // check if supporting opinion has papers associated with it
          if (supporting[opinion] && supporting[opinion].length > 0) {
            const opinionId = `supporting-${index}`;
            const labelWithBreaks = breakLabelByWords(opinion, 3);
            const nodeColor = colors[index % colors.length];
            nodes.push({ id: opinionId, label: labelWithBreaks, color: nodeColor, shape: 'dot' });
            edges.push({ from: 'supporting', to: opinionId });
          }
        });
      }  

      if (viewMode === 'opposing' || viewMode === 'all') {
        Object.keys(opposing).forEach((opinion, index) => {
          // Check if oposing opinion has papers associated with it
          if (opposing[opinion] && opposing[opinion].length > 0) {
            const opinionId = `opposing-${index}`;
            const labelWithBreaks = breakLabelByWords(opinion, 3);
            const nodeColor = colors[(index + 3) % colors.length];
            nodes.push({ id: opinionId, label: labelWithBreaks, color: nodeColor, shape: 'dot' });
            edges.push({ from: 'opposing', to: opinionId });
          }
        });
      }

      const visData = { nodes, edges };
      const options = {
        nodes: { size: 20, font: { size: 16 } },
        edges: { arrows: 'to', color: '#848484' },
        interaction: { hover: true, tooltipDelay: 200 },
        physics: { enabled: true, stabilization: true },
      };

      const network = new Network(networkRef.current, visData, options);

      network.on('click', (params) => {
        if (params.nodes.length > 0) {
          const clickedNodeId = params.nodes[0];
          const isSupportingNode = clickedNodeId.startsWith('supporting-');
          const isOpposingNode = clickedNodeId.startsWith('opposing-');

          let opinion, paperIds;

          if (isSupportingNode) {
            const opinionIndex = clickedNodeId.split('-')[1];
            opinion = Object.keys(supporting)[opinionIndex];
            paperIds = supporting[opinion];
          } else if (isOpposingNode) {
            const opinionIndex = clickedNodeId.split('-')[1];
            opinion = Object.keys(opposing)[opinionIndex];
            paperIds = opposing[opinion];
          }

          if (opinion && paperIds) {
            setSelectedOpinion(opinion);
            const matchedPapers = paperIds.map((paperId) => {
              const paper = graphPapers.find((p) => p.paperId === paperId);
              return paper || null;
            }).filter((p) => p !== null);

            setModalContent(matchedPapers);
            setOpenModal(true);
          }
        }
      });

      return () => network.destroy();
    }
  }, [isLoading, viewMode, supporting, opposing, graphPapers]);  // Depend on isLoading

  const getPapers = () => {
    if (viewMode === 'supporting') {
      return graphPapers.filter((paper) => Object.values(supporting).flat().includes(paper.paperId));
    } else if (viewMode === 'opposing') {
      return graphPapers.filter((paper) => Object.values(opposing).flat().includes(paper.paperId));
    } else {
      return graphPapers.filter((paper) => 
        Object.values(supporting).flat().includes(paper.paperId) ||
        Object.values(opposing).flat().includes(paper.paperId)
      );
    }
  };

  // Display spinner while loading
  if (isLoading) {
    return (
      <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <div>
      <TopNavigation
      currentView={currentView}
      onViewChange={(view) => {
        setCurrentView(view);
        if (view === 'List View'){
            navigate('/opinion-list-view', { state: { ...location.state, currentView: 'List View' } });
        }
      }}
      papers={graphPapers} 
      viewtype={viewType} 
      supporting={supporting} 
      opposing={opposing} 
      prompt={searchPrompt} />

      {/* Dropdown for selecting opinions */}
      <Box sx={{ position: 'absolute', top: '6rem', right: '2rem', display: 'flex', alignItems: 'center' }}>
        <Select
          value={viewMode}
          onChange={(e) => setViewMode(e.target.value)}
          displayEmpty
          inputProps={{ 'aria-label': 'Choose opinion view' }}
          style={{ marginRight: '8px' }}
        >
          <MenuItem value="supporting">Supporting</MenuItem>
          <MenuItem value="opposing">Opposing</MenuItem>
          <MenuItem value="all">All Opinions</MenuItem>
        </Select>
      </Box>

      <Container maxWidth="xl">
        <br />
        <br></br>
        
        <Typography variant="h5" textAlign="left" style={{fontWeight:'lighter', color:'darkblue'}} gutterBottom>
          Results for {formattedPrompt}
        </Typography><br></br>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Typography variant="h6" fontWeight="light" fontSize="16px">
              Top Papers
            </Typography>
            {graphPapers.length > 0 ? (
              <Box sx={{maxWidth:'100%'}}>
                <PaperListOpinion papers={getPapers()} onGenerateSummaryClick={setSelectedPaper} />
              </Box>
            ) : (
              <Typography>No papers found.</Typography>
            )}
          </Grid>
          <Grid item xs={12} md={6}>
            <Typography variant="h6" fontWeight="light" fontSize="16px">
              {viewMode.charAt(0).toUpperCase() + viewMode.slice(1)} Opinions Graph
            </Typography>
            <div
              ref={networkRef}
              style={{
                width: '100%',
                height: '500px',
                border: '1px solid lightgray',
                marginTop: '20px',
              }}
            />
          </Grid>
        </Grid>
      </Container>

      <Modal open={openModal} onClose={() => setOpenModal(false)} aria-labelledby="modal-title" aria-describedby="modal-description">
        <Box sx={modalStyle}>
          <Typography id="modal-title" variant="h6" component="h2" style={{ fontWeight: 'bold' }}>
            {selectedOpinion.replace(/_/g, ' ').charAt(0).toUpperCase() + selectedOpinion.slice(1)}
          </Typography>
          <ul style={{ listStyleType: 'none', paddingLeft: 0 }}>
            {modalContent.map((paper, index) => (
              <li key={index} style={{ marginBottom: '16px' }}>
                <Typography>
                  <a href={paper.url} target="_blank" rel="noopener noreferrer" style={{ textDecoration: 'none', color: 'inherit' }}>
                    <IconButton edge="end" aria-label="pdf" style={{ padding: 0 }}>
                      <PictureAsPdfIcon color="error" style={{ marginLeft: '8px' }} />
                    </IconButton>
                    <span style={{ marginLeft: '16px' }} />
                    <b>{formatAuthors(paper.authors)} , {new Date(paper.publicationDate).getFullYear()}</b>
                  </a>
                  <span style={{ marginLeft: '16px' }} />
                  <span style={{ fontWeight: 300 }}>{paper.title}</span>
                </Typography>
              </li>
            ))}
          </ul>
        </Box>
      </Modal>
    </div>
  );
}

export default GraphViewOpinion;
