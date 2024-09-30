import React, { useState, useEffect } from 'react';
import { Container, Grid, Typography, Slider, FormControl, InputLabel, Select, MenuItem, Paper } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import PaperDetailOpinion from '../../components/PaperDetailOpinion';
import { useSearchParams } from 'react-router-dom';

function ListViewOpinion() {
  const [selectedPaper, setSelectedPaper] = useState(null);
  const [papers, setPapers] = useState({
    supporting: [
      { id: 1, title: "Deep Learning Exploration", year: 2019, citedBy: 100, author: "John Doe" },
      { id: 3, title: "The Future of AI", year: 2018, citedBy: 150, author: "Alice Johnson" }
    ],
    opposing: [
      { id: 2, title: "AI Predictions", year: 2021, citedBy: 200, author: "Jane Smith" }
    ]
  });
  const [originalPapers] = useState(papers);
  const [yearRange, setYearRange] = useState([2018, 2021]);
  const [selectedAuthor, setSelectedAuthor] = useState('');
  const [mostCited, setMostCited] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {
    filterPapers();
  }, [yearRange, selectedAuthor, mostCited]);

  const filterPapers = () => {
    const filteredSupporting = originalPapers.supporting.filter(paper =>
      (selectedAuthor ? paper.author === selectedAuthor : true) &&
      paper.year >= yearRange[0] && paper.year <= yearRange[1]
    );
    const filteredOpposing = originalPapers.opposing.filter(paper =>
      (selectedAuthor ? paper.author === selectedAuthor : true) &&
      paper.year >= yearRange[0] && paper.year <= yearRange[1]
    );

    if (mostCited) {
      filteredSupporting.sort((a, b) => b.citedBy - a.citedBy);
      filteredOpposing.sort((a, b) => b.citedBy - a.citedBy);
    }

    setPapers({
      supporting: filteredSupporting,
      opposing: filteredOpposing
    });
  };

  return (
    <Container maxWidth="xl">
      <Typography variant="h5" textAlign="center" gutterBottom>Research Papers</Typography>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <FormControl fullWidth margin="normal">
            <InputLabel>Author</InputLabel>
            <Select
              value={selectedAuthor}
              label="Author"
              onChange={e => setSelectedAuthor(e.target.value)}
            >
              {['John Doe', 'Jane Smith', 'Alice Johnson'].map(author => (
                <MenuItem key={author} value={author}>{author}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth margin="normal">
            <InputLabel>Most Cited</InputLabel>
            <Select
              value={mostCited}
              label="Most Cited"
              onChange={e => setMostCited(e.target.value)}
            >
              <MenuItem value={true}>Yes</MenuItem>
              <MenuItem value={false}>No</MenuItem>
            </Select>
          </FormControl>
          <Typography gutterBottom>Year Range</Typography>
          <Slider
            value={yearRange}
            onChange={(event, newValue) => setYearRange(newValue)}
            valueLabelDisplay="auto"
            min={2018}
            max={2021}
            marks={[
              { value: 2018, label: '2018' },
              { value: 2021, label: '2021' }
            ]}
          />
        </Grid>
        <Grid item xs={4}>
          <Typography variant="h6">Supporting Papers</Typography>
          <PaperListOpinion papers={papers.supporting} onGenerateSummaryClick={setSelectedPaper} />
        </Grid>
        <Grid item xs={4}>
          <Typography variant="h6">Opposing Papers</Typography>
          <PaperListOpinion papers={papers.opposing} onGenerateSummaryClick={setSelectedPaper} />
        </Grid>
        <Grid item xs={4}>
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
  );
}

export default ListViewOpinion;
