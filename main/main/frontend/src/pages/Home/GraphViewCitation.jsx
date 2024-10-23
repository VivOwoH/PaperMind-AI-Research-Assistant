import React, { useState, useEffect, useRef } from 'react';
import { CircularProgress, Container, Grid, Typography, MenuItem, Select, FormControl, Box } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';
import { DataSet } from 'vis-data';
import { Network } from 'vis-network/standalone';

function GraphViewCitation() {
  const [selectedPaper, setSelectedPaper] = useState(null);
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

    let papersToUse;

    // Select the data based on view type
    if (viewType === 'ALL') {
      papersToUse = citations;  // Use all papers from Citations
    } else if (viewType === 'SUPPORTING') {
      papersToUse = supporting;  // Use supporting papers
    } else {
      papersToUse = opposing;    // Use opposing papers
    }

    // Process each paper in the selected data
    Object.keys(papersToUse).forEach((category) => {
      const papers = papersToUse[category];

      if (Array.isArray(papers)) {
        papers.forEach((paper) => {
          const paperId = typeof paper === 'string' ? paper : paper?.paperId;
          
          // Validate that paperId exists
          if (!paperId || typeof paperId !== 'string' || paperId.trim() === '') {
            console.error(`Invalid or missing paperId in category ${category}:`, paperId);
            return;  // Skip this node if the id is invalid
          }

          // Add the paper node if it hasn't been added yet
          if (!addedNodes.has(paperId)) {
            const label = formatLabel(paperId); // Use formatLabel to create a label
            if (!label || !paperId) {
              console.error(`Invalid paperId or label: paperId=${paperId}, label=${label}`);
              return; // Skip adding invalid nodes
            }

            nodes.add({
              id: paperId,
              label: label,
            });
            addedNodes.add(paperId);
          }

          // Add citation edges if they exist
          if (citations[paperId]) {
            const citingPapers = citations[paperId];

            citingPapers.forEach((citingPaperObj) => {
              const citingPaperId = citingPaperObj.citingPaper.paperId;

              // Validate that citingPaperId exists
              if (!citingPaperId || typeof citingPaperId !== 'string' || citingPaperId.trim() === '') {
                console.error(`Missing citingPaperId for paperId=${paperId}`);
                return; // Skip adding invalid citing papers
              }

              // Add citing paper node if it hasn't been added
              if (!addedNodes.has(citingPaperId)) {
                const citingLabel = formatLabel(citingPaperId);
                if (!citingLabel || !citingPaperId) {
                  console.error(`Invalid citingPaperId or label: citingPaperId=${citingPaperId}, citingLabel=${citingLabel}`);
                  return; // Skip adding invalid nodes
                }

                nodes.add({
                  id: citingPaperId,
                  label: citingLabel,
                });
                addedNodes.add(citingPaperId);
              }

              // Add the edge from citing paper to cited paper
              edges.add({
                from: citingPaperId,
                to: paperId,
              });
            });
          }
        });
      }
    });

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

    new Network(visJsRef.current, data, options);
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
    </div>
  );
}

export default GraphViewCitation;
