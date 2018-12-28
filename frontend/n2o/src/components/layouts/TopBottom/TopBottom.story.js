import React from 'react';
import { storiesOf } from '@storybook/react';
import TopBottom from './TopBottom';
import Section from '../Section';
import Wireframe from '../../snippets/Wireframe/Wireframe';

const stories = storiesOf('Разметка/Страницы', module);

stories.add('Сверху-снизу', () => {
  return (
    <div style={{ width: 300 }}>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-top" title="Header" />
        </div>
      </div>
      <TopBottom>
        <Section place="top">
          <div
            style={{ height: 75, position: 'relative', marginLeft: '-1em', marginRight: '-1em' }}
          >
            <Wireframe title="Top" />
          </div>
        </Section>
        <Section place="bottom">
          <div
            style={{ height: 75, position: 'relative', marginLeft: '-1em', marginRight: '-1em' }}
          >
            <Wireframe title="Bottom" />
          </div>
        </Section>
      </TopBottom>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-bottom" title="Footer" />
        </div>
      </div>
    </div>
  );
});
