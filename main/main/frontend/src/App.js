import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home';
import ListViewCitation from './pages/Home/ListViewCitation';
import ListViewOpinion from './pages/Home/ListViewOpinion';
import GraphViewCitation from './pages/Home/GraphViewCitation';
import GraphViewOpinion from './pages/Home/GraphViewOpinion';
import LoadingPage from './pages/LoadingPage';
import ErrorPage from './pages/ErrorPage';


function App() {

  return (
    <Router>
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/papers-graphview" element={<GraphViewCitation />} />
            <Route path="/list-view" element={<ListViewCitation />}/>
            <Route path="/opinion-list-view" element={<ListViewOpinion />}/>
            <Route path="/papers-opinion-graphview" element={<GraphViewOpinion />}/>
            <Route path="/loading" element={<LoadingPage />} />
            <Route path="/error" element={<ErrorPage />} />
        </Routes>
    </Router>
  );
}

export default App;