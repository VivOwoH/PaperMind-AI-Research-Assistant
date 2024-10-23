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
  const isLoading = location.state?.loading ?? true;
  const [viewType, setViewType] = useState(''); 
  const [searchPrompt, setSearchPrompt] = useState('');
  

  useEffect(() => {
    if (location.state?.graphPapers) {
      setGraphPapers(location.state.graphPapers);
      setViewType(location.state.graphViewType);
    }
    if (location.state?.prompt){
      setSearchPrompt(location.state.prompt);
      console.log(prompt);
    }
  }, [location.state?.graphPapers]);

  const capitalizeWords = (str) => {
    return str
      .split(' ')               
      .map(word => word.charAt(0).toUpperCase() + word.slice(1)) 
      .join(' ');               
  };

  const formattedPrompt = capitalizeWords(searchPrompt);

  if (isLoading) {
    return (
        <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <CircularProgress />
        </Container>
    );  
  }
  
  return (
    <div>
      <TopNavigation currentView={currentView} onViewChange={setCurrentView} papers={graphPapers} viewtype={viewType} prompt={searchPrompt}/>
      <Container maxWidth="xl">
        <br></br>
        <Typography  sx={{ color: 'darkblue', fontWeight: 'light', fontSize:'20px' }}>Showing results for {formattedPrompt} </Typography>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            
            {graphPapers.length > 0 ? (
              <PaperListOpinion papers={graphPapers} onGenerateSummaryClick={setSelectedPaper} />
            ) : (
              <Typography>No papers found.</Typography>
            )}
          </Grid>
          <Grid item xs={12} md={6}>
            {/* <Typography variant="h6">Paper Summary</Typography>
            {selectedPaper ? (
              <PaperDetailOpinion paper={selectedPaper} />
            ) : (
              <Paper style={{ padding: '20px' }}>
                <Typography>Select a paper to view its summary</Typography>
              </Paper>
            )} */}
          </Grid>
        </Grid>
      </Container>
    </div>
  );
}

export default GraphViewCitation;
