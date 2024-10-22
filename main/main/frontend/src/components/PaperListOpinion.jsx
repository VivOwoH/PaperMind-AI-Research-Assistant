import React from 'react';
import { List, ListItem, ListItemText, Collapse, Typography, Link, Box,Divider, IconButton, CircularProgress } from '@mui/material';
import PaperDetailOpinion from './PaperDetailOpinion';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import axios from 'axios';

function PaperListOpinion({ papers, onGenerateSummaryClick }) {
  const [summaryOpen, setSummaryOpen] = React.useState({});
  const [summaries, setSummaries] = React.useState({});
  const [loading, setLoading] = React.useState({});

  const generateAISummary = async (id, abstract) => {
    setSummaryOpen(prev => {
      const isCurrentlyOpen = !prev[id];
      return { ...prev, [id]: isCurrentlyOpen };  
    });

    // summary is not already fetched, make POST request
    if (!summaries[id] && !loading[id]) {
      setLoading(prev => ({ ...prev, [id]: true })); // set loading state for this paper

      try {
        const response = await axios.post('http://localhost:8080/api/generate-summary', {
          abstract: abstract, // send abstract as the request body
        });

        setSummaries(prev => ({
          ...prev,
          [id]: response.data,  // store the fetched summary in state
        }));

      } catch (error) {
        console.error('Error fetching AI summary:', error);
        setSummaries(prev => ({
          ...prev,
          [id]: 'Failed to load summary. Please try again.',
        }));
      } finally {
        setLoading(prev => ({ ...prev, [id]: false })); // reset loading state for this paper
      }
    }
  }

  const onPaperSelect = (id) =>{
    onGenerateSummaryClick(id);
  };

  const formatAuthors = (authors) => {
    return authors.map(author => author.name).join(', ');
  };

  const formatPublicationTypes = (types) => {
    if (!types) {  
      return '';  
    }
    return types.join(' ');
  }

  return (
    <List component="nav" sx={{ width: '100%' }}>
      {papers.map((paper,index) => (
        <React.Fragment key={paper.paperId}>
          <ListItem alignItems="flex-start" sx={{ flexDirection: "column", alignItems: "flex-start", width: '100%', cursor:'pointer','&:hover': {
                backgroundColor: '#f4f4f4' 
              } }} onClick={() => onPaperSelect(paper.paperId)}>
          <Typography
              variant="h6"
              style={{ fontWeight: 'bold', color: '#302C29', fontSize:'20px' }}  
            >
              {paper.title}
            </Typography>
            <ListItemText 
              
              secondary={
                <>
                  <Typography component="span" variant="body2" color="#29436e" fontSize="16px" fontWeight='light'>
                    {formatAuthors(paper.authors)}
                  </Typography>
                  <br />
                  <Typography component="span" variant="body2" color="textPrimary" fontSize="16px">
                    Year: {new Date(paper.publicationDate).getFullYear()} | {formatPublicationTypes(paper.publicationTypes)}
                  </Typography>
                  <br />
                  <Typography component="span" variant="body2" color="textPrimary" fontSize="16px">
                    Cited by: {paper.citationCount}
                  </Typography>
                  <br />
                  {paper.url && (
                  <Link href={paper.url} target="_blank" rel="noopener noreferrer">
                <IconButton color="primary" aria-label="download pdf">
                  <PictureAsPdfIcon />
                </IconButton>
              </Link>
            )}
                </>
              }
            />
            <Typography
              variant="body2"
              color="primary"
              style={{ cursor: 'pointer', marginTop: 8 }}
              onClick={() => generateAISummary(paper.paperId, paper.abstract)}
            >
              {summaryOpen[paper.paperId] ? "Hide AI Summary" : "Show AI Summary"}
            </Typography>
          </ListItem>
          <Collapse in={summaryOpen[paper.paperId]} timeout="auto" unmountOnExit>
            <Box sx={{ maxWidth: '100%', paddingLeft: 4, paddingRight: 4, overflowWrap:'break-word', boxSizing:'border-box' }}>
                {loading[paper.paperId] ? (
                  <CircularProgress size={24} /> // show loading spinner
                ) : summaries[paper.paperId] ? (
                  <PaperDetailOpinion paper={{ ...paper, summary: summaries[paper.paperId] }} /> // pass AI summary to the PaperDetailOpinion
                ) : null}
            </Box>
          </Collapse>
          {index !== papers.length - 1 && <Divider sx={{ mt: 2 }} />}  {/* divider after each item except the last */}
        </React.Fragment>
      ))}
    </List>
  );
}

export default PaperListOpinion;