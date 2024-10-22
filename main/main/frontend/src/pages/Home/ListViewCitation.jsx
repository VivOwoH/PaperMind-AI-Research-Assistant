import React, { useState, useEffect } from 'react';
import { CircularProgress, Container, Grid, Typography, TextField, MenuItem, Box } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';
import axios from 'axios';

function ListViewCitation() {
  const [graphPapers, setGraphPapers] = useState([]); // Store all papers
  const [filteredPapers, setFilteredPapers] = useState([]); // Store filtered papers
  const [currentView, setCurrentView] = useState('List View');
  const [isLoading, setIsLoading] = useState(true); // Local loading state
  const [years, setYears] = useState([]); // Store unique publication years

  // Properly declaring state variables for filters
  const [selectedYear, setSelectedYear] = useState('');
  const [selectedAuthor, setSelectedAuthor] = useState('');
  const [selectedType, setSelectedType] = useState('');

  const location = useLocation();

  useEffect(() => {
    if (location.state?.papers) {
      setGraphPapers(location.state.papers);
      setFilteredPapers(location.state.papers);
      setIsLoading(false);

      const uniqueYears = [...new Set(location.state.papers.map(paper => new Date(paper.publicationDate).getFullYear()))];
      setYears(uniqueYears.sort((a, b) => b - a));
    }
  }, [location.state?.papers]);

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
      <TopNavigation currentView={currentView} onViewChange={setCurrentView} />
      <Container maxWidth="xl">
        <Grid container spacing={3}>
          {/* Filters on the left with fixed positioning */}
          <Grid item xs={3} style={{ position: 'fixed', top: '100px', left: 0, height: '80vh', overflowY: 'auto' }}>
            <Box sx={{ paddingRight: '20px' }}>
              <Typography variant="h6" sx={{ color: 'grey', fontWeight: 'bold' }}>Filters</Typography>
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
              <TextField
                fullWidth
                label="Filter by Author"
                variant="outlined"
                value={selectedAuthor}
                onChange={(e) => setSelectedAuthor(e.target.value)}
                style={{ marginBottom: '20px' }}
              />
              <TextField
                fullWidth
                select
                label="Filter by Type"
                variant="outlined"
                value={selectedType}
                onChange={(e) => setSelectedType(e.target.value)}
                style={{ marginBottom: '20px' }}
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
          <Grid item xs={9} style={{ marginLeft: '25%', overflow: 'auto', height: '80vh' }}>
            <br></br>
            <Typography variant="h6" sx={{ color: 'grey', fontWeight: 'bold' }}>Papers List</Typography>
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
