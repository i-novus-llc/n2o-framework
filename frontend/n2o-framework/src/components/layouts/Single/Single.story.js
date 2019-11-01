import React from 'react';
import { storiesOf } from '@storybook/react';
import Single from './Single';
import Section from '../Section';
import Wireframe from '../../snippets/Wireframe/Wireframe';

const stories = storiesOf('Разметка/Страницы', module);

stories.add('Одна колонка', () => {
  return (
    <div style={{ width: 300 }}>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-top" title="Header" />
        </div>
      </div>
      <Single>
        <Section place="single">
          <div
            style={{
              height: 150,
              position: 'relative',
              marginLeft: '-1em',
              marginRight: '-1em',
            }}
          >
            <Wireframe title="single" />
          </div>
        </Section>
      </Single>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-bottom" title="Footer" />
        </div>
      </div>
    </div>
  );
});
