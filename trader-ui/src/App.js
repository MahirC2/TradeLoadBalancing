import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PriceChecker from './components/PriceChecker';
import OrderForm from './components/OrderForm';

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<PriceChecker />} />
          <Route path="/order" element={<OrderForm />} />
        </Routes>
      </Router>
  );
}

export default App;
