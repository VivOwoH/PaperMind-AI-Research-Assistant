import * as React from 'react';
import { useEffect } from 'react';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import { useSearch } from '../context/SearchContext';

export default function ColorToggleButton() {
  // access data and update function from context
  const {searchData, updateSearchData} = useSearch();
  const [graphView, setGraphView] = React.useState('citation');  // Default to 'citation'

  useEffect(() => {
    setGraphView(searchData.graphViewType);// Ensure default is set from the context
  }, [searchData.graphViewType]);

  const handleChange = (event, newGraphView) => {
    if (newGraphView !== null) {  // Prevents setting to null if the same button is clicked
      setGraphView(newGraphView);
      // print toggle for debugging
      //console.log('toggle:', newGraphView);
      updateSearchData('graphViewType', newGraphView);//update alignment in the context
    }
  };

  return (
    <ToggleButtonGroup
      value={graphView}
      exclusive
      onChange={handleChange}
      aria-label="Platform"
      
    >
      <ToggleButton value="CITATION" style={graphView === 'CITATION' ? {borderRadius: '20px 0px 0px 20px', backgroundColor:'#f2bd29', color:'#26336e' } : {borderRadius: '20px 0px 0px 20px', backgroundColor:'lightgray'}}>
        Citation
      </ToggleButton>
      <ToggleButton value="OPINION" style={graphView === 'OPINION' ? {borderRadius: '0px 20px 20px 0px',backgroundColor:'#f2bd29' , color:'#26336e'} : {borderRadius: '0px 20px 20px 0px', backgroundColor:'lightgray'}}>
        Opinion
      </ToggleButton>
    </ToggleButtonGroup>
  );
}
