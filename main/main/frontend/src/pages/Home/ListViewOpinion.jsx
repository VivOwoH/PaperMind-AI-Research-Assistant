import React, { useState, useEffect } from 'react';
import { CircularProgress, Container, Grid, Typography, Paper } from '@mui/material';
import PaperListOpinion from '../../components/PaperListOpinion';
import PaperDetailOpinion from '../../components/PaperDetailOpinion';
import TopNavigation from '../../components/TopNavigation';
import { useLocation } from 'react-router-dom';

function ListViewOpinion() {
    const [selectedPaper, setSelectedPaper] = useState(null);
    const location = useLocation();
    const [graphPapers, setGraphPapers] = useState([]);
    const [supportPapers, setSupportPapers] = useState({});
    const [supportingPapersList, setSupportingPapersList] = useState([]);
    const [opposingPapers, setOpposingPapers] = useState({});
    const [opposingPapersList, setOpposingPapersList] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

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
        const calculateSupportingPapers = () => {
            if (!supportPapers || typeof supportPapers !== 'object' || !graphPapers) {
                return [];
            }
          
            const result = [];
            Object.keys(supportPapers).forEach(opinion => {
                const paperIds = supportPapers[opinion];
                paperIds.forEach(paperId => {
                    const paper = graphPapers.find(p => p.paperId === paperId);
                    if (paper) {
                        result.push({ paper, opinion });
                    }
                });
            });
            return result;
        };

        const calculateOpposingPapers = () => {
            if (!opposingPapers || typeof opposingPapers !== 'object' || !graphPapers) {
                return [];
            }
          
            const result = [];
            Object.keys(opposingPapers).forEach(opinion => {
                const paperIds = opposingPapers[opinion];
                paperIds.forEach(paperId => {
                    const paper = graphPapers.find(p => p.paperId === paperId);
                    if (paper) {
                        result.push({ paper, opinion });
                    }
                });
            });
            return result;
        };

        setSupportingPapersList(calculateSupportingPapers());
        setOpposingPapersList(calculateOpposingPapers());
    }, [supportPapers, opposingPapers, graphPapers]);

    if (isLoading) {
        return (
            <Container maxWidth="xl" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <CircularProgress />
            </Container>
        );
    }

    return (
        <div>
            <TopNavigation />
            <Container maxWidth="xl">
                <Typography variant="h5" textAlign="left" gutterBottom>Search Results</Typography>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={6}>
                        <Typography variant="h6">Supporting Papers List</Typography>
                        {supportingPapersList.length > 0 ? (
                            supportingPapersList.map((entry, index) => (
                                <Paper key={index} style={{ padding: '10px', marginBottom: '10px' }}>
                                    <Typography variant="subtitle1" color="textSecondary">{entry.opinion}</Typography>
                                    <Typography variant="h6">{entry.paper.title}</Typography>
                                    <Typography>Authors: {entry.paper.authors.map(author => author.name).join(', ')}</Typography>
                                    <Typography>Cited by: {entry.paper.citationCount}</Typography>
                                </Paper>
                            ))
                        ) : (
                            <Typography>No supporting papers found.</Typography>
                        )}
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <Typography variant="h6">Opposing Papers List</Typography>
                        {opposingPapersList.length > 0 ? (
                            opposingPapersList.map((entry, index) => (
                                <Paper key={index} style={{ padding: '10px', marginBottom: '10px' }}>
                                    <Typography variant="subtitle1" color="textSecondary">{entry.opinion}</Typography>
                                    <Typography variant="h6">{entry.paper.title}</Typography>
                                    <Typography>Authors: {entry.paper.authors.map(author => author.name).join(', ')}</Typography>
                                    <Typography>Cited by: {entry.paper.citationCount}</Typography>
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
