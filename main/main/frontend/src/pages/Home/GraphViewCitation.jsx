import React, { useState, useEffect, useRef } from 'react';
import { CircularProgress, Container, Grid, Typography, MenuItem, Select, FormControl, Box, Modal } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';
import { DataSet } from 'vis-data';
import { Network } from 'vis-network/standalone';

function GraphViewCitation() {
  const [selectedPaper, setSelectedPaper] = useState(null);
  const [selectedPaperDetails, setSelectedPaperDetails] = useState(null); // Store details of clicked paper
  const [isModalOpen, setIsModalOpen] = useState(false); // State for modal visibility
  const [citingPapers, setCitingPapers] = useState([]); // State to store citing papers
  const location = useLocation();
  const [graphPapers, setGraphPapers] = useState({});
  const [viewType, setViewType] = useState(location.state?.selectedFilter || 'ALL');
  const [searchPrompt, setSearchPrompt] = useState('');
  const [currentView, setCurrentView] = useState('Graph View');
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
      setIsLoading(false);
    };

    loadData();
  }, [location.state]);

  const drawGraph = () => {
    if (!graphPapers || !visJsRef.current) return;
  
    const nodes = new DataSet();
    const edges = new DataSet();
    const addedNodes = new Set();
    const addedEdges = new Set(); // Track added edges
  
    // Helper function to add a node if it hasn't been added yet
    const addNode = (paperId, label) => {
      if (!addedNodes.has(paperId)) {
        nodes.add({ id: paperId, label: label });
        addedNodes.add(paperId);
      }
    };
  
    // Helper function to format citing papers
    const formatCitingLabel = (citingPaperObj) => {
      const citingAuthors = citingPaperObj.citingPaper.authors || [];
      const firstAuthor = citingAuthors.length > 0 ? citingAuthors[0].name : 'Unknown';
      const publicationDate = citingPaperObj.citingPaper.publicationDate;
      const year = publicationDate ? new Date(publicationDate).getFullYear() : 'Unknown';
      return `${firstAuthor} (${year})`;
    };
  
    // Helper function to add edges between citing and cited papers
    const addEdges = (paperId) => {
      if (citations[paperId]) {
        const citingPapers = citations[paperId];
        citingPapers.forEach((citingPaperObj) => {
          const citingPaperId = citingPaperObj.citingPaper.paperId;
          const citingLabel = formatCitingLabel(citingPaperObj);
          if (!citingPaperId) return; // Skip if citing paper ID is invalid
  
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
            addedEdges.add(edgeId); // Track this edge as added
          }
        });
      }
    };
  
    // Process data based on viewType
    if (viewType === 'ALL') {
      // Handle Citations (flat structure)
      Object.keys(citations).forEach((paperId) => {
        const label = formatLabel(paperId);
        addNode(paperId, label);
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
            addNode(paperId, label);
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
        scaling: { min: 10, max: 50 },
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
        const paperDetails = graphPapers.find(p => p.paperId === paperId);
        const citingPapersList = citations[paperId]?.map(citingPaperObj => {
          const citingPaper = graphPapers.find(p => p.paperId === citingPaperObj.citingPaper.paperId);
          return citingPaper ? formatCitingLabel(citingPaperObj) : "Unknown";
        }) || [];

        setSelectedPaperDetails(paperDetails);
        setCitingPapers(citingPapersList);
        setIsModalOpen(true);
      }
    });
  };
  
  // Function to format node labels with first author and year
  const formatLabel = (paperId) => {
    // Find the paper by ID and return first author + year as the label
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
        onViewChange={setCurrentView}
        papers={graphPapers}
        viewtype={viewType}
        prompt={searchPrompt}
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
        <Box sx={{ padding: 3, backgroundColor: 'white', margin: '5% auto', maxWidth: 600 }}>
          <Typography variant="h6">
            {selectedPaperDetails ? `${selectedPaperDetails.authors[0]?.name || 'Unknown'} (${new Date(selectedPaperDetails.publicationDate).getFullYear()})` : 'Paper Details'}
          </Typography>
          <Typography variant="body1" sx={{ marginTop: 2 }}>
            {selectedPaperDetails?.title || 'No title available'}
          </Typography>
          <Typography variant="subtitle1" sx={{ marginTop: 2 }}>
            Cited By:
          </Typography>
          <ul>
            {citingPapers.map((citingPaper, index) => (
              <li key={index}>{citingPaper}</li>
            ))}
          </ul>
        </Box>
      </Modal>
    </div>
  );
}

export default GraphViewCitation;
