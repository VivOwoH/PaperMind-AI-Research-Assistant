import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';

function PaperDetailOpinion({ paper }) {
  return (
    <Card>
      <CardContent>
        <Typography variant="h6" component="div">
          {/* AI Summary for {paper.title} */}
        </Typography>
        <Typography variant="body2">
          {paper.summary || "AI summary not available."}
        </Typography>
      </CardContent>
    </Card>
  );
}

export default PaperDetailOpinion;
