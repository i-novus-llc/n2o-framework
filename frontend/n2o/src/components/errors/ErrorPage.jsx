import React from 'react';
import PropTypes from 'prop-types';

function ErrorPage({ status, error }) {
  return (
    <div className="container d-flex align-items-center justify-content-center">
      <div>
        <h1 style={{ fontSize: '10rem' }}>{status}</h1>
        <span style={{ fontSize: '2rem' }}>{error}</span>
      </div>
    </div>
  );
}

ErrorPage.propTypes = {
  status: PropTypes.number,
  error: PropTypes.string,
};

export default ErrorPage;
