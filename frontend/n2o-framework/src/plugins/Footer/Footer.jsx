import React from 'react';

function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="n2o-footer bg-dark py-2">
      <div className="container-fluid">
        <span className="text-muted text-left">
          N2O 7.1 &copy; 2013-{currentYear}
        </span>
      </div>
    </footer>
  );
}

export default Footer;
