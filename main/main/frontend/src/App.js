import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home';
import ListViewOpinion from './pages/Home/ListViewOpinion';


function App() {

  return (
    <Router>
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/papers" element={<ListViewOpinion />} /> 
        </Routes>
    </Router>
  );
}

export default App;