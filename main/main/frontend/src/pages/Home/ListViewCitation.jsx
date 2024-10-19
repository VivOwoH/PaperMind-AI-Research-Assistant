import React, { useState, useEffect } from 'react';
import { CircularProgress, Container, Grid, Typography, Paper, TextField, MenuItem } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import PaperDetailOpinion from '../../components/PaperDetailOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';

function ListViewCitation() {
  const [selectedPaper, setSelectedPaper] = useState(null);
  const location = useLocation();
  const [graphPapers, setGraphPapers] = useState([]); // Store all papers
  const [filteredPapers, setFilteredPapers] = useState([]); // Store filtered papers
  const [currentView, setCurrentView] = useState('List View');
  const [isLoading, setIsLoading] = useState(true); // Local loading state
  const [years, setYears] = useState([]); // Store unique publication years

  // Properly declaring state variables for filters
  const [selectedYear, setSelectedYear] = useState('');
  const [selectedAuthor, setSelectedAuthor] = useState('');
  const [selectedType, setSelectedType] = useState('');

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
      filtered = filtered.filter(paper => paper.publicationTypes.includes(selectedType));
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

  if (isLoading) {
    return (
      <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <CircularProgress />
      </Container>
    );  
  }

  const handlePaperClick = (paperId) => {
    const paper = filteredPapers.find(p => p.paperId === paperId);
    setSelectedPaper(paper);
  };

  return (
    <div>
      <TopNavigation currentView={currentView} onViewChange={setCurrentView} />
      <Container maxWidth="xl">
        <Typography variant="h5" textAlign="left" gutterBottom>Search Results</Typography>
        <Grid container spacing={2}>
          <Grid item xs={3}>
            <TextField
              fullWidth
              select
              label="Filter by Year"
              variant="outlined"
              value={selectedYear}
              onChange={(e) => setSelectedYear(e.target.value)}
            >
              <MenuItem value="">All Years</MenuItem>
              {years.map(year => (
                <MenuItem key={year} value={year}>{year}</MenuItem>
              ))}
            </TextField>
          </Grid>
          <Grid item xs={3}>
            <TextField
              fullWidth
              label="Filter by Author"
              variant="outlined"
              value={selectedAuthor}
              onChange={(e) => setSelectedAuthor(e.target.value)}
            />
          </Grid>
          <Grid item xs={3}>
            <TextField
              fullWidth
              select
              label="Filter by Type"
              variant="outlined"
              value={selectedType}
              onChange={(e) => setSelectedType(e.target.value)}
            >
              <MenuItem value="">All Types</MenuItem>
              <MenuItem value="JournalArticle">Journal</MenuItem>
              <MenuItem value="Conference">Conference</MenuItem>
              <MenuItem value="Book">Book</MenuItem>
              <MenuItem value="Review">Review</MenuItem>
            </TextField>
          </Grid>
        </Grid>
        <Grid container spacing={3} style={{ marginTop: '20px' }}>
          <Grid item xs={6} sx={{ overflow: 'auto', height: '90vh' }}>
            <Typography variant="h6" sx={{ color: 'grey', fontWeight: 'bold' }}>Papers List</Typography>
            {filteredPapers.length > 0 ? (
              <PaperListOpinion papers={filteredPapers} onGenerateSummaryClick={handlePaperClick} />
            ) : (
              <Typography>No papers found.</Typography>
            )}
          </Grid>
          <Grid item xs={6} sx={{ position: 'fixed', top: '25vh', right: 0, width: '50%', height: `calc(100vh - 100px)`, overflowY: 'auto' }}>
            <Typography variant="h6" sx={{ color: 'grey', fontWeight: 'bold' }}>Paper Summary</Typography><br />
            {selectedPaper ? (
              <PaperDetailOpinion paper={selectedPaper} />
            ) : (
              <Typography>Select a paper to view details.</Typography>
            )}
          </Grid>
        </Grid>
      </Container>
    </div>
  );
}

export default ListViewCitation;
