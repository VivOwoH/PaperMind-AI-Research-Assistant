import React, { useState, useEffect } from 'react';
import { CircularProgress, Container, Grid, Typography, Paper, Link, IconButton, Collapse, Box, Chip } from '@mui/material';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import PaperDetailOpinion from '../../components/PaperDetailOpinion';

function ListViewOpinion() {
    const [selectedPaper, setSelectedPaper] = useState(null);
    const location = useLocation();
    const [graphPapers, setGraphPapers] = useState([]);
    const [supportPapers, setSupportPapers] = useState({});
    const [supportingPapersList, setSupportingPapersList] = useState([]);
    const [opposingPapers, setOpposingPapers] = useState({});
    const [opposingPapersList, setOpposingPapersList] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [summaryOpen, setSummaryOpen] = React.useState({});

    // Define a consistent style for paper cards
    const paperCardStyle = {
        padding: '20px',
        margin: '10px 0',
        backgroundColor:'#f2f2f2',
        minHeight: '150px',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        '&:hover': {
            backgroundColor: '#e6e6e6' 
        }
    };

    useEffect(() => {
        if (location.state?.papers) {
            setGraphPapers(location.state.papers);
        }
        if (location.state?.supporting) {
            setSupportPapers(location.state.supporting);
        }
        if (location.state?.opposing) {
            setOpposingPapers(location.state.opposing);
        }
        setIsLoading(false);
    }, [location.state]);

    useEffect(() => {
        const calculatePapers = (papersMap) => {
            if (!papersMap || typeof papersMap !== 'object' || !graphPapers) {
                return [];
            }
          
            const result = [];
            Object.keys(papersMap).forEach(opinion => {
                const paperIds = papersMap[opinion];
                paperIds.forEach(paperId => {
                    const paper = graphPapers.find(p => p.paperId === paperId);
                    if (paper) {
                        result.push({ paper, opinion });
                    }
                });
            });
            return result;
        };

        setSupportingPapersList(calculatePapers(supportPapers));
        setOpposingPapersList(calculatePapers(opposingPapers));
    }, [supportPapers, opposingPapers, graphPapers]);

    const formatPublicationTypes = (types) => {
      if (!types) {  
        return '';  
      }
      return types.join(' ');
    }

    const handleSummaryToggle = (id) => {
      setSummaryOpen(prev => {  
        const isCurrentlyOpen = !prev[id];
        return { ...prev, [id]: isCurrentlyOpen };  
      });
    };

    if (isLoading) {
        return (
            <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <CircularProgress />
            </Container>
        );
    }
    // Opinion to color mapping
    const opinionColorMap = {
      Supporting: 'success',  // Green color for supporting
      Opposing: 'error',      // Red color for opposing
      Neutral: 'default'      // Yellow for neutral (or any other opinions you may have)
    };

  const getChipColor = (opinion) => opinionColorMap[opinion] || 'default';


    return (
        <div>
            <TopNavigation />
            <Container maxWidth="xl">
              <br></br>
                <Typography variant="h5" textAlign="left" gutterBottom>Search Results</Typography><br></br>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={6}>
                        <Typography variant="h6" fontWeight={'bold'} color={"grey"}>Supporting Papers</Typography>
                        {supportingPapersList.length > 0 ? (
                            supportingPapersList.map((entry, index) => (
                                <Paper key={index} sx={paperCardStyle}>
                                  
                                    <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                                    <Chip label={entry.opinion} color={getChipColor('Supporting')} size="small" sx={{ marginBottom: '10px' }} />
                                    </div>
                                    
                                    <Typography variant="h6"style={{ fontWeight: 'bold', color: '#302C29', fontSize:'20px' }}  >{entry.paper.title}</Typography>
                                    <Typography component="span" variant="body2" color="#29436e" fontSize="16px" fontWeight='light'>
                                      {entry.paper.authors.map(author => author.name).join(', ')}</Typography>
                                    <Typography component="span" variant="body2" color="textPrimary" fontSize="16px">
                                    Year: {new Date(entry.paper.publicationDate).getFullYear()} | {formatPublicationTypes(entry.paper.publicationTypes)}
                                    </Typography>
                                    
                                  <Typography>Cited by: {entry.paper.citationCount}</Typography>
                                    {entry.paper.url && (
                                      <Link href={entry.paper.url} target="_blank" rel="noopener noreferrer">
                                      <IconButton color="primary" aria-label="download pdf">
                                      <PictureAsPdfIcon />
                                      </IconButton>
                                      </Link>
                                    )}
                                    <Typography
                                    variant="body2"
                                    color="primary"
                                    style={{ cursor: 'pointer', marginTop: 8 }}
                                    onClick={() => handleSummaryToggle(entry.paper.paperId)}
                                  >
                                  {summaryOpen[entry.paper.paperId] ? "Hide AI Summary" : "Show AI Summary"}
                                  </Typography>
                                  <Collapse in={summaryOpen[entry.paper.paperId]} timeout="auto" unmountOnExit>
                                  <Box sx={{ width: '90%', paddingLeft: 4, paddingRight: 4 }}>
                                  <PaperDetailOpinion paper={entry.paper} />
                                  </Box>
                                  </Collapse>
                          </Paper>
                            ))
                        ) : (
                            <Typography>No supporting papers found.</Typography>
                        )}
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <Typography variant="h6" fontWeight={'bold'} color={"grey"}>Opposing Papers</Typography>
                        {opposingPapersList.length > 0 ? (
                            opposingPapersList.map((entry, index) => (
                                <Paper key={index} sx={paperCardStyle}>
                                    <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                                    <Chip label={entry.opinion} color={getChipColor('Opposing')} size="small" sx={{ marginBottom: '10px' }} />
                                    </div>
                                    <Typography variant="h6"style={{ fontWeight: 'bold', color: '#302C29', fontSize:'20px' }}  >{entry.paper.title}</Typography>
                                    <Typography component="span" variant="body2" color="#29436e" fontSize="16px" fontWeight='light'>
                                      {entry.paper.authors.map(author => author.name).join(', ')}</Typography>
                                    <Typography component="span" variant="body2" color="textPrimary" fontSize="16px">
                                    Year: {new Date(entry.paper.publicationDate).getFullYear()} | {formatPublicationTypes(entry.paper.publicationTypes)}
                                    </Typography>
                                    <Typography>Cited by: {entry.paper.citationCount}</Typography>
                                    {entry.paper.url && (
                                    <Link href={entry.paper.url} target="_blank" rel="noopener noreferrer">
                                    <IconButton color="primary" aria-label="download pdf">
                                    <PictureAsPdfIcon />
                                    </IconButton>
                                    </Link>
                                    )}
                                    <Typography
                                      variant="body2"
                                      color="primary"
                                      style={{ cursor: 'pointer', marginTop: 8 }}
                                      onClick={() => handleSummaryToggle(entry.paper.paperId)}
                                    >
                                    {summaryOpen[entry.paper.paperId] ? "Hide AI Summary" : "Show AI Summary"}
                                    </Typography>
                                    <Collapse in={summaryOpen[entry.paper.paperId]} timeout="auto" unmountOnExit>
                                      <Box sx={{ width: '90%', paddingLeft: 4, paddingRight: 4 }}>
                                      <PaperDetailOpinion paper={entry.paper} />
                                    </Box>
                                    </Collapse>
                                </Paper>
                            ))
                        ) : (
                            <Typography>No opposing papers found.</Typography>
                        )}
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default ListViewOpinion;
