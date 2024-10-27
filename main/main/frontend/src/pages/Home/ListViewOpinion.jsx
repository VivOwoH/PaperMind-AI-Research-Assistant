import React, { useState, useEffect } from 'react';
import { CircularProgress, Container, Grid, Typography, Paper, Link, IconButton, Collapse, Box, Chip } from '@mui/material';
import TopNavigation from '../../components/TopNavigation';
import { useLocation, useNavigate } from 'react-router-dom';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import PaperDetailOpinion from '../../components/PaperDetailOpinion';
import axios from 'axios';  // Import axios for making requests

function ListViewOpinion() {
    // State declarations
    const [selectedPaper, setSelectedPaper] = useState(null);
    const location = useLocation();
    const [graphPapers, setGraphPapers] = useState([]);
    const [supportPapers, setSupportPapers] = useState({});
    const [supportingPapersList, setSupportingPapersList] = useState([]);
    const [opposingPapers, setOpposingPapers] = useState({});  // Fix: added state for opposing papers
    const [opposingPapersList, setOpposingPapersList] = useState([]);  // Fix: added state for opposing papers list
    const [isLoading, setIsLoading] = useState(true);
    const [summaryOpen, setSummaryOpen] = useState({});
    const [summaries, setSummaries] = useState({});  // Store the generated summaries
    const [loading, setLoading] = useState({});  // Track loading state per paper
    const [searchPrompt, setSearchPrompt] = useState('');
    const [currentView, setCurrentView] = useState('List View');
    const navigate = useNavigate();

    // Define a consistent style for paper cards
    const paperCardStyle = {  // Fix: Define paperCardStyle properly
        padding: '20px',
        margin: '10px 0',
        backgroundColor: '#f2f2f2',
        minHeight: '150px',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        '&:hover': {
            backgroundColor: '#e6e6e6'
        }
    };

    const capitalizeWords = (str) => {
        return str
          .split(' ')               
          .map(word => word.charAt(0).toUpperCase() + word.slice(1)) 
          .join(' ');               
    };
    
    const formattedPrompt = capitalizeWords(searchPrompt);

    useEffect(() => {
        if (location.state?.papers) {
            setGraphPapers(location.state.papers);
        }
        if (location.state?.supporting) {
            setSupportPapers(location.state.supporting);
        }
        if (location.state?.opposing) {
            setOpposingPapers(location.state.opposing);  // Fix: Use setOpposingPapers to update opposing papers
        }
        if (location.state?.prompt) {
            setSearchPrompt(location.state.prompt);
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
        setOpposingPapersList(calculatePapers(opposingPapers));  // Fix: Use setOpposingPapersList to update opposing papers list
    }, [supportPapers, opposingPapers, graphPapers]);

    const formatPublicationTypes = (types) => {
      if (!types) {  
        return '';  
      }
      return types.join(' ');
    };

    // Function to generate AI summary when requested
    const generateAISummary = async (id, abstract) => {
        if (!summaries[id] && !loading[id]) {  // Only fetch if not already loaded
            setLoading(prev => ({ ...prev, [id]: true }));  // Set loading state

            try {
                const response = await axios.post('http://localhost:8080/api/generate-summary', {
                    abstract: abstract,  // Pass the paper's abstract
                });

                setSummaries(prev => ({
                    ...prev,
                    [id]: response.data,  // Store the fetched summary
                }));
            } catch (error) {
                console.error('Error fetching AI summary:', error);
                setSummaries(prev => ({
                    ...prev,
                    [id]: 'Failed to load summary. Please try again.',  // Error handling
                }));
            } finally {
                setLoading(prev => ({ ...prev, [id]: false }));  // Reset loading state
            }
        }
    };

    const handleSummaryToggle = (id, abstract) => {
      setSummaryOpen(prev => {  
        const isCurrentlyOpen = !prev[id];
        if (isCurrentlyOpen) {  // Only generate summary when opening
            generateAISummary(id, abstract);  // Trigger the summary generation
        }
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
      Supporting: 'success',  
      Opposing: 'error',      
      Neutral: 'default'      
    };

    const getChipColor = (opinion) => opinionColorMap[opinion] || 'default';

    return (
        <div>
            <TopNavigation 
            currentView={currentView}
            onViewChange={(view) => {
                setCurrentView(view);
                if (view === 'Graph View'){
                    navigate('/papers-opinion-graphview', { state: { ...location.state, currentView: 'Graph View' } });
                }
            }}/>
            <Container maxWidth="xl">
                <br />
                <Typography variant="h5" textAlign="left" style={{ fontWeight: 'lighter', color: 'darkblue' }} gutterBottom>
                    Results for {formattedPrompt}
                </Typography>
                <br />
                <Grid container spacing={3}>
                    <Grid item xs={12} md={6}>
                        <Typography variant="h6" fontWeight="light" fontSize="16px">Supporting Papers</Typography>
                        {supportingPapersList.length > 0 ? (
                            supportingPapersList.map((entry, index) => (
                                <Paper key={index} sx={paperCardStyle}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                        <Chip label={entry.opinion.replace(/_/g, ' ')} color={getChipColor('Supporting')} size="small" sx={{ marginBottom: '10px' }} />
                                    </div>

                                    <Typography variant="h6" style={{ fontWeight: 'bold', color: '#302C29', fontSize: '20px' }}>{entry.paper.title}</Typography>
                                    <Typography component="span" variant="body2" color="#29436e" fontSize="16px" fontWeight='light'>
                                        {entry.paper.authors.map(author => author.name).join(', ')}
                                    </Typography>
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
                                        onClick={() => handleSummaryToggle(entry.paper.paperId, entry.paper.abstract)}
                                    >
                                        {summaryOpen[entry.paper.paperId] ? "Hide AI Summary" : "Show AI Summary"}
                                    </Typography>
                                    <Collapse in={summaryOpen[entry.paper.paperId]} timeout="auto" unmountOnExit>
                                        <Box sx={{ width: '90%', paddingLeft: 4, paddingRight: 4 }}>
                                            {loading[entry.paper.paperId] ? (
                                                <CircularProgress size={24} />
                                            ) : summaries[entry.paper.paperId] ? (
                                                <PaperDetailOpinion paper={{ ...entry.paper, summary: summaries[entry.paper.paperId] }} />
                                            ) : null}
                                        </Box>
                                    </Collapse>
                                </Paper>
                            ))
                        ) : (
                            <Typography sx={{marginTop:'3%'}}>No supporting papers found.</Typography>
                        )}
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <Typography variant="h6" fontWeight="light" fontSize="16px">Opposing Papers</Typography>
                        {opposingPapersList.length > 0 ? (
                            opposingPapersList.map((entry, index) => (
                                <Paper key={index} sx={paperCardStyle}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                        <Chip label={entry.opinion} color={getChipColor('Opposing')} size="small" sx={{ marginBottom: '10px' }} />
                                    </div>

                                    <Typography variant="h6" style={{ fontWeight: 'bold', color: '#302C29', fontSize: '20px' }}>{entry.paper.title}</Typography>
                                    <Typography component="span" variant="body2" color="#29436e" fontSize="16px" fontWeight='light'>
                                        {entry.paper.authors.map(author => author.name).join(', ')}
                                    </Typography>
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
                                        onClick={() => handleSummaryToggle(entry.paper.paperId, entry.paper.abstract)}
                                    >
                                        {summaryOpen[entry.paper.paperId] ? "Hide AI Summary" : "Show AI Summary"}
                                    </Typography>
                                    <Collapse in={summaryOpen[entry.paper.paperId]} timeout="auto" unmountOnExit>
                                        <Box sx={{ width: '90%', paddingLeft: 4, paddingRight: 4 }}>
                                            {loading[entry.paper.paperId] ? (
                                                <CircularProgress size={24} />
                                            ) : summaries[entry.paper.paperId] ? (
                                                <PaperDetailOpinion paper={{ ...entry.paper, summary: summaries[entry.paper.paperId] }} />
                                            ) : null}
                                        </Box>
                                    </Collapse>
                                </Paper>
                            ))
                        ) : (
                            
                            <Typography sx={{marginTop:'3%', color:'darkblue'}}>No opposing papers found.</Typography>
                        )}
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default ListViewOpinion;
