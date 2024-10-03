import React from 'react';
import { List, ListItem, ListItemText, Collapse, Button, ListItemButton } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';

function PaperListOpinion({ papers, onGenerateSummaryClick }) {
  const [open, setOpen] = React.useState({});

  const handleClick = (id) => {
    setOpen(prev => ({ ...prev, [id]: !prev[id] }));
  };

  return (
    <List component="nav">
      {papers.map((paper) => (
        <React.Fragment key={paper.id || paper.paperId}> {/* Ensure the key is unique */}
          <ListItemButton onClick={() => handleClick(paper.id || paper.paperId)}>
            <ListItemText primary={paper.title} />
            {open[paper.id || paper.paperId] ? <ExpandLessIcon /> : <ExpandMoreIcon />}
          </ListItemButton>
          <Collapse in={open[paper.id || paper.paperId]} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              <ListItem sx={{ pl: 4 }}>
                <ListItemText secondary={`Author: ${paper.author}`} />
              </ListItem>
              <ListItem sx={{ pl: 4 }}>
                <ListItemText secondary={`Year: ${paper.year}`} />
              </ListItem>
              <ListItem sx={{ pl: 4 }}>
                <Button variant="contained" onClick={() => onGenerateSummaryClick(paper)}>
                  Generate AI Summary
                </Button>
              </ListItem>
            </List>
          </Collapse>
        </React.Fragment>
      ))}
    </List>
  );
}

export default PaperListOpinion;
