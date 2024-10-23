import React, { useEffect } from 'react';
import { Card, CardContent, Typography, IconButton, Tooltip } from '@mui/material';
import FileCopyIcon from '@mui/icons-material/FileCopy';
import SaveIcon from '@mui/icons-material/Save';
import { gapi } from 'gapi-script';
import { Document, Packer, Paragraph, TextRun } from 'docx'; // docx plugin

function PaperDetailOpinion({ paper }) {
  const SCOPES = "https://www.googleapis.com/auth/drive.file";
  const CLIENT_ID = process.env.REACT_APP_CLIENT_ID; // client id
  const API_KEY = process.env.REACT_APP_API_KEY; //  API key

  // initiate Google API client
  useEffect(() => {
    const start = () => {
      gapi.client.init({
        apiKey: API_KEY,
        clientId: CLIENT_ID,
        scope: SCOPES
      });
    };
    gapi.load('client:auth2', start);
  }, [[API_KEY, CLIENT_ID]]);

  // create .docx file with the ai summary content
  const createDocx = async () => {
    const doc = new Document({
      sections: [
        {
          properties: {},
          children: [
            new Paragraph({
              children: [
                new TextRun({
                  text: paper.title,
                  bold: true,
                  size: 28, 
                }),
              ],
            }),
            new Paragraph({ // empty paragraph to add a space between title and abstract
              children: [
                new TextRun({
                  text: "", 
                }),
              ],
            }),
            new Paragraph({
              children: [
                new TextRun({
                  text: "Abstract:",
                  bold: true,
                  size: 24, 
                }),
              ],
            }),
            new Paragraph({
              children: [
                new TextRun({
                  text: paper.summary, // ai summary form the paper
                  size: 22, 
                }),
              ],
            }),
          ],
        },
      ],
    });

    // generate a blob of the .docx document
    const blob = await Packer.toBlob(doc);
    console.log('Generated Blob:', blob); // log the generated blob
    return blob;
  };

  // save the generated .docx file to the root of Google Drive
  const saveToDrive = async () => {
    gapi.auth2.getAuthInstance().signIn().then(async () => {
      const accessToken = gapi.auth.getToken().access_token;

      // log the access token
      console.log('Access Token:', accessToken);

      if (!accessToken) {
        alert('Failed to retrieve access token.');
        return;
      }

      // create a blob of .docx file
      const docxBlob = await createDocx(); 

      // metadata for the Google Drive upload (save to root)
      const metadata = {
        name: `${paper.title}.docx`, // save file with a .docx extension
        mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        parents: ['root'] // save the file to the root directory of the user's Drive
      };

      // create a form to upload the file
      const formData = new FormData();
      formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));
      formData.append('file', docxBlob); // append the .docx file blob

      // upload the .docx file to Google Drive
      fetch('https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart&fields=id', {
        method: 'POST',
        headers: new Headers({ Authorization: 'Bearer ' + accessToken }), // pass the access token for authorization
        body: formData // formData that contains the .docx file and metadata
      })
      .then((res) => res.json())
      .then((value) => {
        console.log('API Response:', value); // log the full API response
        if (value.id) {
          alert('File saved to your Google Drive with ID: ' + value.id);
          // Redirect to the Google Drive file's URL
          window.location.href = `https://drive.google.com/file/d/${value.id}/view`;
        } else {
          alert('File upload failed. No file ID returned.');
        }
      })
      .catch((error) => {
        console.error('Error saving file to Google Drive:', error);
      });
    });
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(paper.summary);
    alert("Abstract copied to clipboard!");
  };

  return (
    <Card>
      <CardContent>
        <Typography variant="h6" component="div">
          AI Summary for {paper.title}
        </Typography>
        <Typography variant="body2">
          {paper.summary}
        </Typography>
        <Tooltip title="Copy Abstract">
          <IconButton onClick={copyToClipboard} sx={{ color: '#4287f5' }}>
            <FileCopyIcon />
          </IconButton>
        </Tooltip>
        <Tooltip title="Save to Google Drive">
          <IconButton onClick={saveToDrive} sx={{ color: '#4287f5' }}>
            <SaveIcon />
          </IconButton>
        </Tooltip>
      </CardContent>
    </Card>
  );
}

export default PaperDetailOpinion;
