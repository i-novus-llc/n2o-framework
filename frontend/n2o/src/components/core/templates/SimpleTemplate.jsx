import React from 'react';
import SimpleHeader from '../../../plugins/Header/SimpleHeader/SimpleHeader';
import Footer from '../../../plugins/Footer/Footer';
import MenuContainer from '../../../plugins/Menu/MenuContainer';

export default function SimpleTemplate({ children }) {
  return (
    <div className="application">
      <div className="application-body container-fluid">{children}</div>
    </div>
  );
}
