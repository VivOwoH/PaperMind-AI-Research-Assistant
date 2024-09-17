import * as React from 'react';
import { useEffect } from 'react';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';

export default function ColorToggleButton() {
  const [alignment, setAlignment] = React.useState('citation');  // Default to 'citation'

  useEffect(() => {
    setAlignment('citation'); // Ensure default is set to 'citation' after component mounts
  }, []);

  const handleChange = (event, newAlignment) => {
    if (newAlignment !== null) {  // Prevents setting to null if the same button is clicked
      setAlignment(newAlignment);
      console.log('toggle:', newAlignment);
    }
  };

  return (
    <ToggleButtonGroup
      value={alignment}
      exclusive
      onChange={handleChange}
      aria-label="Platform"
      
    >
      <ToggleButton value="citation" style={alignment === 'citation' ? { boxShadow: '0px 0px 10px #555', backgroundColor:'blue', color:'white' } : {backgroundColor:'lightgray'}}>Citation</ToggleButton>
      <ToggleButton value="opinion" style={alignment === 'opinion' ? { boxShadow: '0px 0px 10px #555',backgroundColor:'blue' , color:'white'} : {backgroundColor:'lightgray'}}>Opinion</ToggleButton>
    </ToggleButtonGroup>
  );
}
