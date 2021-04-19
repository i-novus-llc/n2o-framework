import React from 'react';
import PropTypes from 'prop-types';
import { getContext } from 'recompose';

function Footer({ version }) {
  const currentYear = new Date().getFullYear();

  const { N2O_ELEMENT_VISIBILITY } = window;
  let style = {};

  if (N2O_ELEMENT_VISIBILITY && N2O_ELEMENT_VISIBILITY.footer === false) {
    style = { ...style, display: 'none' };
  }

  return (
    <footer className="n2o-footer bg-dark py-2" style={style}>
      <div className="container-fluid">
        <span className="text-muted text-left">
          N2O {version} &copy; 2013-{currentYear}
        </span>
      </div>
    </footer>
  );
}

export default getContext({
  version: PropTypes.string,
})(Footer);
