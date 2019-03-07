import React from 'react';
import cx from 'classnames';
const s = {};

class Footer extends React.Component {
  render() {
    return (
      <footer className={cx(s.footer, 'bg-dark', 'py-2')}>
        <div className="container-fluid">
          <span className="text-muted text-left">N2O 7.1 &copy; 2013-2019</span>
        </div>
      </footer>
    );
  }
}

export default Footer;
