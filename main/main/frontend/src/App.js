import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home';
import ListViewCitation from './pages/Home/ListViewCitation';
import ListViewOpinion from './pages/Home/ListViewOpinion';
import GraphViewCitation from './pages/Home/GraphViewCitation';


function App() {

  return (
    <Router>
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/papers-graphview" element={<GraphViewCitation />} /> 
            <Route path="/list-view" element={<ListViewCitation />}></Route>
        </Routes>
    </Router>
  );
}

export default App;