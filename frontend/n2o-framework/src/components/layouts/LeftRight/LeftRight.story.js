import React from 'react';
import { storiesOf } from '@storybook/react';
import LeftRight from './LeftRight';
import Section from '../Section';
import Wireframe from '../../snippets/Wireframe/Wireframe';

const stories = storiesOf('Разметка/Страницы', module);

stories.add('Две колонки', () => {
  return (
    <div style={{ width: 300 }}>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-top" title="Header" />
        </div>
      </div>
      <LeftRight>
        <Section place="left">
          <div className="position-relative" style={{ height: 150 }}>
            <Wireframe title="Left" />
          </div>
        </Section>
        <Section place="right">
          <div className="position-relative" style={{ height: 150 }}>
            <Wireframe title="Right" />
          </div>
        </Section>
      </LeftRight>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-bottom" title="Footer" />
        </div>
      </div>
    </div>
  );
});
