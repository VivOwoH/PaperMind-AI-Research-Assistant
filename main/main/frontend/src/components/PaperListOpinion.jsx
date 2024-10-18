import React from 'react';
import { List, ListItem, ListItemText, Collapse, Typography, Link, Box,Divider, IconButton } from '@mui/material';
import PaperDetailOpinion from './PaperDetailOpinion';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';

function PaperListOpinion({ papers, onGenerateSummaryClick }) {
  const [summaryOpen, setSummaryOpen] = React.useState({});

  const handleSummaryToggle = (id) => {
    setSummaryOpen(prev => {  
      const isCurrentlyOpen = !prev[id];

      onGenerateSummaryClick(id);
      return { ...prev, [id]: isCurrentlyOpen };  
    });
  };
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
                backgroundColor: '#f4f4f4' // Optional: Change background on hover for better UX
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
              onClick={() => handleSummaryToggle(paper.paperId)}
            >
              {summaryOpen[paper.paperId] ? "Hide AI Summary" : "Show AI Summary"}
            </Typography>
          </ListItem>
          <Collapse in={summaryOpen[paper.paperId]} timeout="auto" unmountOnExit>
            <Box sx={{ width: '100%', paddingLeft: 4, paddingRight: 4 }}>
              <PaperDetailOpinion paper={paper} />
            </Box>
          </Collapse>
          {index !== papers.length - 1 && <Divider sx={{ mt: 2 }} />}  {/* divider after each item except the last */}
        </React.Fragment>
      ))}
    </List>
  );
}

export default PaperListOpinion;