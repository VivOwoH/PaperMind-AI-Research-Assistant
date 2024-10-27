import React, { useState, useEffect, useRef } from 'react';
import { CircularProgress, Container, Grid, Typography, MenuItem, Select, FormControl, Box, Modal, Link } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation, useNavigate } from 'react-router-dom';
import { DataSet } from 'vis-data';
import { Network } from 'vis-network/standalone';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';


function GraphViewCitation() {
  const [selectedPaper, setSelectedPaper] = useState(null);
  const [selectedPaperDetails, setSelectedPaperDetails] = useState(null); // Store details of clicked paper
  const [isModalOpen, setIsModalOpen] = useState(false); // State for modal visibility
  const [citingPapers, setCitingPapers] = useState([]); // State to store citing papers
  const location = useLocation();
  const navigate = useNavigate(); // Use navigate for switching views
  const [graphPapers, setGraphPapers] = useState({});
  const [viewType, setViewType] = useState(location.state?.selectedFilter || 'ALL');
  const [searchPrompt, setSearchPrompt] = useState('');
  const [currentView, setCurrentView] = useState('Graph View');
  const [graphViewType, setGraphViewType] = useState('Graph View');
  const visJsRef = useRef(null); 
  const [citations, setCitations] = useState({});
  const [supporting, setSupporting] = useState({});
  const [opposing, setOpposing] = useState({});
  const [isLoading, setIsLoading] = useState(true);

  // Capitalize words function
  const capitalizeWords = (str) => {
    return str
      .split(' ')
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  };

  // Effect to load data from location.state
  useEffect(() => {
    const loadData = () => {
      if(location.state?.papers){
        setGraphPapers(location.state.papers);
      }
      if (location.state?.graphPapers) {
        setGraphPapers(location.state.graphPapers);
      }
      if (location.state?.citations) {
        setCitations(location.state.citations);
      }
      if (location.state?.supporting) {
        setSupporting(location.state.supporting);
      }
      if (location.state?.opposing) {
        setOpposing(location.state.opposing);
      }
      if (location.state?.prompt) {
        setSearchPrompt(location.state.prompt);
      }
      if (location.state?.graphViewType) {
        setGraphViewType(location.state.graphViewType);
      }
      if (location.state?.currentView){
        setCurrentView(location.state.currentView);
      }
      setIsLoading(false);
    };

    loadData();
  }, [location.state]);

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

  const drawGraph = () => {
    if (!graphPapers || !visJsRef.current) return;
  
    const nodes = new DataSet();
    const edges = new DataSet();
    const addedNodes = new Set();
    const addedEdges = new Set(); // Track added edges
    const nodesWithIncomingEdges = new Set(); // Track nodes with incoming edges
  
    // Helper function to add a node if it hasn't been added yet
    const addNode = (paperId, label, citationCount = 0) => {
      // Scale node size based on citation count (min: 10, max: 50)
      const size = Math.min(50, Math.max(10, 10 + citationCount * 2));
      if (!addedNodes.has(paperId)) {
        nodes.add({ id: paperId, label: label, size: size });
        addedNodes.add(paperId);
      }
    };
  
    // Helper function to format citing papers
    const formatCitingLabel = (citingPaperObj) => {
      const citingAuthors = citingPaperObj.citingPaper.authors || [];
      const firstAuthor = citingAuthors.length > 0 ? citingAuthors[0].name : 'Unknown';
      const publicationDate = citingPaperObj.citingPaper.publicationDate;
      const year = publicationDate ? new Date(publicationDate).getFullYear() : 'Unknown';
      return `${firstAuthor} ${year}`;
    };
  
    // Modified function to fetch citation details (including PDF link)
    const formatCitationDetails = (citingPaperObj) => {
      const citingAuthors = citingPaperObj.citingPaper.authors || [];
      const firstAuthor = citingAuthors.length > 0 ? citingAuthors[0].name : 'Unknown';
      const publicationDate = citingPaperObj.citingPaper.publicationDate;
      const year = publicationDate ? new Date(publicationDate).getFullYear() : 'Unknown';
      const pdfLink = citingPaperObj.citingPaper.openAccessPdf?.url || null;
  
      return {
        label: `${firstAuthor} ${year}`,
        pdfLink: pdfLink,
      };
    };
  
    // Helper function to add edges between citing and cited papers
    const addEdges = (paperId) => {
      if (citations[paperId]) {
        const citingPapers = citations[paperId];
        if (Array.isArray(citingPapers) && citingPapers.length > 0){
          citingPapers.forEach((citingPaperObj) => {
            const citingPaperId = citingPaperObj.citingPaper.paperId;
            const citingLabel = formatCitingLabel(citingPaperObj);
            if (!citingPaperId) return; // skip if citing paper ID is invalid
    
            // Add the citing paper node if not already added
            if (!addedNodes.has(citingPaperId)) {
              addNode(citingPaperId, citingLabel);
            }
    
            // Create an edge identifier to check for duplicates
            const edgeId = `${citingPaperId}->${paperId}`;
    
            // Add the edge from the citing paper to the cited paper only if it doesn't already exist
            if (!addedEdges.has(edgeId)) {
              edges.add({
                from: citingPaperId,
                to: paperId,
              });
              addedEdges.add(edgeId); 
    
              // Mark the cited paper (paperId) as having an incoming edge
              nodesWithIncomingEdges.add(paperId);
            }
          });
        }   
      }
    };
  
    // Process data based on viewType
    if (viewType === 'ALL') {
      // Handle Citations (flat structure)
      Object.keys(citations).forEach((paperId) => {
        const label = formatLabel(paperId);
        const citationCount = citations[paperId]?.length || 0;
        addNode(paperId, label, citationCount);
        addEdges(paperId);
      });
    } else {
      // Handle Supporting or Opposing (nested structure)
      const dataToUse = viewType === 'SUPPORTING' ? supporting : opposing;
  
      Object.keys(dataToUse).forEach((category) => {
        const papers = dataToUse[category];
        if (Array.isArray(papers)) {
          papers.forEach((paperId) => {
            const label = formatLabel(paperId);
            const citationCount = citations[paperId]?.length || 0;
            addNode(paperId, label, citationCount);
            addEdges(paperId);
          });
        }
      });
    }
  
    // Use nodes and edges to render the graph
    const data = { nodes, edges };
    const options = {
      nodes: {
        shape: 'dot',
        font: { size: 12 },
      },
      edges: { arrows: { to: true } },
      physics: { barnesHut: { gravitationalConstant: -20000, springLength: 150 } },
    };
  
    const network = new Network(visJsRef.current, data, options);
    // Add event listener for node click to show modal
    network.on('click', (params) => {
      if (params.nodes.length > 0) {
        const paperId = params.nodes[0];
  
        // Check if the clicked node has incoming edges
        if (nodesWithIncomingEdges.has(paperId)) {
          const paperDetails = graphPapers.find(p => p.paperId === paperId);
          const citingPapersList = citations[paperId]?.map(citingPaperObj => {
            const citingDetails = formatCitationDetails(citingPaperObj);
            return citingDetails;
          }) || [];
  
          setSelectedPaperDetails(paperDetails);
          setCitingPapers(citingPapersList);
          setIsModalOpen(true);
        }
      }
    });
  };
  
  // function to format node labels with first author and year
  const formatLabel = (paperId) => {
    // find the paper by ID and return first author + year as the label
    const paper = graphPapers.find(p => p.paperId === paperId);
    if (paper) {
      const firstAuthor = paper.authors[0]?.name || 'Unknown';
      const year = new Date(paper.publicationDate).getFullYear();
      return `${firstAuthor} (${year})`;
    }
    return paperId;
  };

  // Render graph when graphPapers or viewType changes
  useEffect(() => {
    drawGraph();
  }, [graphPapers, viewType]);

  if (isLoading) {
    return (
      <Container
        maxWidth="xl"
        style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
      >
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
              navigate('/list-view', { state: { ...location.state, currentView: 'List View' } });
          }
        }}
        papers={graphPapers}
        viewtype={graphViewType}
        prompt={searchPrompt}
        citations={citations}
        supporting={supporting}
        opposing={opposing}
      />
      <Container maxWidth="xl">
        <br />
        <Typography sx={{ color: 'darkblue', fontWeight: 'light', fontSize: '20px' }}>
          Showing results for {capitalizeWords(searchPrompt)}
        </Typography>

        {/* Dropdown for switching between ALL, SUPPORTING, OPPOSING */}
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '1rem' }}>
          <FormControl>
            <Select
              value={viewType}
              onChange={(e) => setViewType(e.target.value)}
              displayEmpty
            >
              <MenuItem value="ALL">All</MenuItem>
              <MenuItem value="SUPPORTING">Supporting</MenuItem>
              <MenuItem value="OPPOSING">Opposing</MenuItem>
            </Select>
          </FormControl>
        </Box>

        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            {Object.keys(graphPapers).length > 0 ? (
              <PaperListOpinion papers={graphPapers} onGenerateSummaryClick={setSelectedPaper} />
            ) : (
              <Typography>No papers found.</Typography>
            )}
          </Grid>
          <Grid item xs={12} md={6}>
            <div ref={visJsRef} style={{ height: '600px', border: '1px solid lightgray' }}></div>
          </Grid>
        </Grid>
      </Container>

      {/* Modal for paper details */}
      <Modal
        open={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        aria-labelledby="paper-details-modal"
      >
        <Box sx={modalStyle}>
          {selectedPaperDetails && (
            <>
              <Typography variant="h6" sx={{ fontWeight: 'bold', color:'black'}}>
                {selectedPaperDetails.authors[0]?.name || 'Unknown'} {new Date(selectedPaperDetails.publicationDate).getFullYear()}
              </Typography>
              <Typography variant="body1" sx={{ marginTop: 1 , fontWeight: 'light'}}>
                {selectedPaperDetails.title || 'No title available'}
              </Typography>
              {selectedPaperDetails.openAccessPdf?.url && (
              <Link href={selectedPaperDetails.openAccessPdf.url} target="_blank" rel="noopener noreferrer">
              <PictureAsPdfIcon style={{ fontSize: '1.5rem', color: 'red' }} />
              </Link>
              )}

            </>
          )}
          <Typography variant="subtitle1" component="h2" sx={{ marginTop: 2, fontWeight: 'light' }}>
          Cited By:
          </Typography>
          <ul style={{ padding: 0, margin: '10px 0', listStyleType: 'none' }}>
          {citingPapers.map((citingPaper, index) => (
          <li key={index} style={{ marginBottom: '8px'}}>
            <span style={{ fontStyle: 'italic', fontWeight: 'light', color: 'darkblue' }}>{citingPaper.label}</span>
            {citingPaper.pdfLink && (
              <>
              &nbsp;
              <Link href={citingPaper.pdfLink} target="_blank" rel="noopener noreferrer">
                <PictureAsPdfIcon style={{ fontSize: '1rem', verticalAlign: 'middle', color: 'red' }} />
              </Link>
            </>
          )}
          </li>
        ))}
        </ul>
        </Box>
      </Modal>
    </div>
  );
}

export default GraphViewCitation;
