import React from 'react';
import { storiesOf } from '@storybook/react';

import Wireframe from '../../snippets/Wireframe/Wireframe';

const stories = storiesOf('Разметка/Приложения', module);

stories.add('Шапка-тело-подвал', () => {
  return (
    <div style={{ width: 300 }}>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="d-10" title="Header" />
        </div>
      </div>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 150 }}>
          <Wireframe title="Content" />
        </div>
      </div>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="d-30" title="Footer" />
        </div>
      </div>
    </div>
  );
});

stories.add('Меню слева с шапкой-телом-подвалом', () => {
  return (
    <div style={{ width: 300 }}>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="d-10" title="Header" />
        </div>
      </div>
      <div className="row">
        <div className="col-md-4 position-relative" style={{ height: 150 }}>
          <Wireframe title="Sidebar" />
        </div>
        <div className="col-md-8 position-relative" style={{ height: 150 }}>
          <Wireframe className="l-20" title="Content" />
        </div>
      </div>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="d-30" title="Footer" />
        </div>
      </div>
    </div>
  );
});
