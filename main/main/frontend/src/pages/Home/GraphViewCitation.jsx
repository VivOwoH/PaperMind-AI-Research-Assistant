import React, { useState, useEffect } from 'react';
import { CircularProgress, Container, Grid, Typography, Paper } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import PaperDetailOpinion from '../../components/PaperDetailOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';

function GraphViewCitation() {
  const [selectedPaper, setSelectedPaper] = useState(null);
  const location = useLocation();
  const [graphPapers, setGraphPapers] = useState([]); // Start with an empty array
  const [currentView, setCurrentView] = useState('Graph View');
  // const [loading, setLoading] = useState(true); // Assume loading until proven otherwise
  const isLoading = location.state?.loading ?? true;
  


  useEffect(() => {
    if (location.state?.graphPapers) {
        setGraphPapers(location.state.graphPapers);
    }
  }, [location.state?.graphPapers]);

  if (isLoading) {
    return (
        <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <CircularProgress />
        </Container>
    );  
  }
  
  return (
    <div>
      <TopNavigation currentView={currentView} onViewChange={setCurrentView} />
      <Container maxWidth="xl">
        <Typography variant="h5" textAlign="left" gutterBottom>Results for </Typography>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            {/* TODO show keywords of search */}
            <Typography variant="h6">Top 20 papers</Typography>
            {graphPapers.length > 0 ? (
              <PaperListOpinion papers={graphPapers} onGenerateSummaryClick={setSelectedPaper} />
            ) : (
              <Typography>No papers found.</Typography>
            )}
          </Grid>
          <Grid item xs={12} md={6}>
            <Typography variant="h6">Paper Summary</Typography>
            {selectedPaper ? (
              <PaperDetailOpinion paper={selectedPaper} />
            ) : (
              <Paper style={{ padding: '20px' }}>
                <Typography>Select a paper to view its summary</Typography>
              </Paper>
            )}
          </Grid>
        </Grid>
      </Container>
    </div>
  );
}

export default GraphViewCitation;
