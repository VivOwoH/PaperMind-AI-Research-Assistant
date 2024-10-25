import React, { useState, useEffect } from 'react';
import { CircularProgress, Container, Grid, Typography, TextField, MenuItem, Box } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import axios from 'axios';


function ListViewCitation() {
  const [graphPapers, setGraphPapers] = useState([]); // Store all papers
  const [filteredPapers, setFilteredPapers] = useState([]); // Store filtered papers
  const [currentView, setCurrentView] = useState('List View');
  const [isLoading, setIsLoading] = useState(true); // Local loading state
  const [years, setYears] = useState([]); // Store unique publication years
  const navigate = useNavigate();

  // Properly declaring state variables for filters
  const [selectedYear, setSelectedYear] = useState('');
  const [selectedAuthor, setSelectedAuthor] = useState('');
  const [selectedType, setSelectedType] = useState('');
  const location = useLocation();
  const [searchPrompt, setSearchPrompt] = useState('');
  

  useEffect(() => {
    if (location.state?.papers) {
      setGraphPapers(location.state.papers);
      setFilteredPapers(location.state.papers);
      setIsLoading(false);

      const uniqueYears = [...new Set(location.state.papers.map(paper => new Date(paper.publicationDate).getFullYear()))];
      setYears(uniqueYears.sort((a, b) => b - a));
    }
    if (location.state?.prompt){
      setSearchPrompt(location.state.prompt);
    }
    console.log(searchPrompt);
  }, [location.state?.papers]);

  const capitalizeWords = (str) => {
    return str
      .split(' ')               
      .map(word => word.charAt(0).toUpperCase() + word.slice(1)) 
      .join(' ');               
  };

  const formattedPrompt = capitalizeWords(searchPrompt);

  const applyFilters = () => {
    let filtered = [...graphPapers];

    if (selectedYear) {
      filtered = filtered.filter(paper => new Date(paper.publicationDate).getFullYear() === parseInt(selectedYear));
    }
    if (selectedAuthor) {
      filtered = filtered.filter(paper => paper.authors.some(author => author.name.includes(selectedAuthor)));
    }
    if (selectedType) {
      filtered = filtered.filter(paper => paper.publicationTypes && paper.publicationTypes.includes(selectedType));
    }

    setFilteredPapers(filtered);
  };

  useEffect(() => {
    if (selectedYear || selectedAuthor || selectedType) {
      applyFilters();
    } else {
      setFilteredPapers(graphPapers); // Reset to initial state when all filters are cleared
    }
  }, [selectedYear, selectedAuthor, selectedType, graphPapers]);

  const generateAISummary = async (paperId, abstract) => {
    try {
      const response = await axios.post('http://localhost:8080/api/generate-summary', { abstract });
      console.log('AI Summary:', response.data);
    } catch (error) {
      console.error('Error fetching AI summary:', error);
    }
  };

  if (isLoading) {
    return (
      <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <div>
      <TopNavigation currentView={currentView} 
      onViewChange={(view) => {
        setCurrentView(view);
        if (view === 'Graph View') {
            // navigate back to the Graph View and pass the current state
            navigate('/papers-graphview', { state: { ...location.state, currentView: 'Graph View' } });
        }
    }}
      />
      <Container maxWidth="xl">
        <Grid container spacing={3}>
          {/* Filters on the left with fixed positioning */}
          <Grid item xs={3} style={{ position: 'fixed', top: '100px', left: 0, height: '80vh', overflowY: 'auto' }}>
            <Box sx={{ paddingRight: '20px' }}>
              <Typography variant="h6" sx={{ color: 'black', fontWeight: 'light' }}>Apply Filters</Typography>
              <br></br>
              <Typography sx={{ color: 'black', fontWeight: 'light' }}>Year</Typography>
              <TextField
                fullWidth
                select
                label="Filter by Year"
                variant="outlined"
                value={selectedYear}
                onChange={(e) => setSelectedYear(e.target.value)}
                style={{ marginBottom: '20px' }}
              >
                <MenuItem value="">All Years</MenuItem>
                {years.map(year => (
                  <MenuItem key={year} value={year}>{year}</MenuItem>
                ))}
              </TextField>
              <Typography sx={{ color: 'black', fontWeight: 'light' }}>Author</Typography>
              <TextField
                fullWidth
                label="Filter by Author"
                variant="outlined"
                value={selectedAuthor}
                onChange={(e) => setSelectedAuthor(e.target.value)}
                style={{ marginBottom: '20px' }}
              />
              <Typography sx={{ color: 'black', fontWeight: 'light' }}>Publication Type</Typography>
              <TextField
                fullWidth
                select
                label="Filter by Type"
                variant="outlined"
                value={selectedType}
                onChange={(e) => setSelectedType(e.target.value)}
                style={{ marginBottom: '20px'}}
              >
                <MenuItem value="">All Types</MenuItem>
                <MenuItem value="JournalArticle">Journal</MenuItem>
                <MenuItem value="Conference">Conference</MenuItem>
                <MenuItem value="Book">Book</MenuItem>
                <MenuItem value="Review">Review</MenuItem>
              </TextField>
            </Box>
          </Grid>

          {/* Papers List on the right */}
          <Grid item xs={9} style={{ marginLeft: '25%', marginTop:'2.2%', height: 'calc(100vh - 10vh)', overflowY: 'auto' }}>
            <br></br>
            <Typography  sx={{ color: 'darkblue', fontWeight: 'light', fontSize:'20px' }}>Showing results for {formattedPrompt} </Typography>
            {filteredPapers.length > 0 ? (
              <PaperListOpinion 
                papers={filteredPapers} 
                onGenerateSummaryClick={(paperId, abstract) => generateAISummary(paperId, abstract)} 
              />
            ) : (
              <Typography>No papers found.</Typography>
            )}
          </Grid>
        </Grid>
      </Container>
    </div>
  );
}

export default ListViewCitation;
