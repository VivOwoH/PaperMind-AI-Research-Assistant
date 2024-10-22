import React, { useState, useEffect, useRef } from 'react';
import { CircularProgress, Container, Grid, Typography, Modal, Box } from '@mui/material';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import IconButton from '@mui/material/IconButton';
import PaperListOpinion from '../../components/PaperListOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';
import { Network } from 'vis-network/standalone/umd/vis-network.min.js';



function GraphViewOpinion() {
  const [selectedPaper, setSelectedPaper] = useState(null);
  const location = useLocation();
  const [graphPapers, setGraphPapers] = useState([]); 
  const [currentView, setCurrentView] = useState('Graph View');
  const isLoading = location.state?.loading ?? true;
  const [viewType, setViewType] = useState(''); 
  const [supporting, setSupporting] = useState({});
  const [opposing, setOpposing] = useState({});
  // network graph
  const networkRef = useRef(null);
  // modal state
  const [openModal, setOpenModal] = useState(false);
  // stores paper list of clicked opinions
  const [modalContent, setModalContent] = useState([]);
  // stores the selected opinion for display in popup
  const [selectedOpinion, setSelectedOpinion] = useState('');


  // modal styles
  const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '60%',
    bgcolor: 'background.paper',
    boxShadow: 24,
    borderRadius:'1.5rem',
    p: 4,
    overflow: 'auto',
    maxHeight: '60vh', // ensure the modal doesn't exceed 80% of the screen height

  };

  // paper list scrollable box styles
  const paperListStyle = {
    maxHeight: '60vh',
    overflowY: 'auto', 
    padding: '10px',
  };

  const formatAuthors = (authors) => {
    if (authors && authors.length > 0) {
      return authors[0].name;  // Return the first author's name
    }
    return 'Unknown';  // Return 'Unknown' if no authors are found
  };

  useEffect(() => {
    if (location.state?.graphPapers) {
        setGraphPapers(location.state.graphPapers);
    }
    if (location.state?.supporting) {
        setSupporting(location.state.supporting);
        
    }
    if (location.state?.opposing) {
        setOpposing(location.state.opposing);
        
    }
    setViewType(location.state.graphViewType);
  }, [location.state?.graphPapers]);

  useEffect(() => {
    // render the supporting opinions graph
    if (networkRef.current && Object.keys(supporting).length > 0) {
      const nodes = [];
      const edges = [];

      // set of different colors to rotate through for nodes
      const colors = ['#A3D5A6', '#F28C8C', '#F2C94C', '#56CCF2', '#BB6BD9', '#FF7F50'];

      // add the supporting node
      nodes.push({ id: 'supporting', label: 'Supporting', color: '#239178', shape: 'circle', font:{bold:true,color:'#ffffff'}});

      // function to break label into lines by words for better viewing
      const breakLabelByWords = (label, maxWordsPerLine) => {
        const words = label.split(' ');
        let lines = [];
        for (let i = 0; i < words.length; i += maxWordsPerLine) {
          lines.push(words.slice(i, i + maxWordsPerLine).join(' '));
        }
        return lines.join('\n');
      };

      // add supporting opinion nodes and edges
      Object.keys(supporting).forEach((opinion, index) => {
        const opinionId = `supporting-${index}`;
        const labelWithBreaks = breakLabelByWords(opinion, 3);  // break label by 3 words
        const nodeColor = colors[index % colors.length];  // cycle through colors
        nodes.push({ id: opinionId, label: labelWithBreaks, color: nodeColor, shape: 'dot' });
        edges.push({ from: 'supporting', to: opinionId });
      });

      const visData = { nodes, edges };
      const options = {
        nodes: {
          size: 20,
          font: {
            size: 16,
          },
        },
        edges: {
          arrows: 'to',
          color: '#848484',
        },
        interaction: {
          hover: true,
          tooltipDelay: 200,
        },
        physics: {
          enabled: true,
          stabilization: true,
        },
      };

      // initialize the Vis.js network
      const network = new Network(networkRef.current, visData, options);

      // add an event to open model when a node is clicked
      network.on('click', (params) => {
        if (params.nodes.length > 0) {
          const clickedNodeId = params.nodes[0];
          if (clickedNodeId.startsWith('supporting-')) {
            const opinionIndex = clickedNodeId.split('-')[1];
            const opinion = Object.keys(supporting)[opinionIndex];
            setSelectedOpinion(opinion);  // set the clicked opinion
            const paperIds = supporting[opinion];  // get paper IDs related to the clicked opinion
            
            // get full paper details by matching paper IDs with `graphPapers`
            const matchedPapers = paperIds.map(paperId => {
              const paper = graphPapers.find(p => p.paperId === paperId);
              return paper ? paper : null;  
            }).filter(p => p !== null);  // remove any null values

            setModalContent(matchedPapers);  // set the full paper details in modal
            setOpenModal(true);  // open the modal
          
             // Get the position of the clicked node
            const nodePosition = network.getBoundingBox(clickedNodeId);
            const canvasPosition = network.canvasToDOM({ x: nodePosition.left, y: nodePosition.top });
          }
        }
      });

      return () => {
        network.destroy();
      };
    }
  }, [supporting, graphPapers]);

  if (isLoading) {
    return (
        <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <CircularProgress />
        </Container>
    );  
  }
  
  return (
    <div>
      <TopNavigation currentView={currentView} onViewChange={setCurrentView} papers={graphPapers} viewtype={viewType} supporting={supporting} opposing={opposing}/>
      <Container maxWidth="xl">
        <br></br>
        <Typography variant="h5" textAlign="left" gutterBottom>Results for </Typography>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            {/* TODO show keywords of search */}
            <Typography variant="h6" fontWeight="bold" fontSize="16px">Top 20 papers</Typography>
            {graphPapers.length > 0 ? (
              <PaperListOpinion papers={graphPapers} onGenerateSummaryClick={setSelectedPaper} />
            ) : (
              <Typography>No papers found.</Typography>
            )}

          </Grid>
          <Grid item xs={12} md={6}>
          <Typography variant="h6" fontWeight="bold" fontSize="16px">Supporting Opinions Graph</Typography>
            <div
              ref={networkRef}
              style={{
                width: '100%',
                height: '500px',
                border: '1px solid lightgray',
                marginTop: '20px',
              }}
            ></div>
          </Grid>
        </Grid>
      </Container>
      {/* Modal for displaying supporting papers */}
      <Modal
        open={openModal}
        onClose={() => setOpenModal(false)}
        aria-labelledby="modal-title"
        aria-describedby="modal-description"
      >
        <Box sx={modalStyle}>
          <Typography id="modal-title" variant="h6" component="h2" style={{fontWeight:'bold'}}>
            Supporting {selectedOpinion}
          </Typography>
          <ul style={{ listStyleType: 'none', paddingLeft: 0 }}>
            {modalContent.map((paper, index) => (
              <li key={index} style={{ marginBottom: '16px' }}>
                <Typography>
                
                <a href={paper.url} target="_blank" rel="noopener noreferrer" style={{ textDecoration: 'none', color: 'inherit' }}>
                <IconButton edge="end" aria-label="pdf" style={{ padding: 0 }}>
                <PictureAsPdfIcon color="error" style={{ marginLeft: '8px' }} />
                </IconButton>
                <span style={{ marginLeft: '16px' }}></span>
                {/* Authors and Year */}
                <b>{formatAuthors(paper.authors)} , {new Date(paper.publicationDate).getFullYear()}</b>
                {/* Clickable PDF Icon */}
                </a>
                {/* Paper Title */}
                <span style={{ marginLeft: '16px' }}></span>
                <span style={{ fontWeight: 300 }}>{paper.title}</span>
                </Typography>
              </li>
            ))}
            <br></br>
          </ul>
        </Box>
      </Modal>
     
    </div>
  );
}

export default GraphViewOpinion;
