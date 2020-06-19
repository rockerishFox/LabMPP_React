import React from "react";
// import logo from "./logo.svg";
import "./App.css";
import Navbar from "./components/navbar";
import Buttons from "./components/buttons";

function App() {
  return (
    <React.Fragment>
      <Navbar />
      <main className="container">
        <Buttons />
      </main>
    </React.Fragment>
  );
}

export default App;
