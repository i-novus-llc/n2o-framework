import React from 'react';
import { storiesOf } from '@storybook/react';
import LeftTopBottom from './LeftTopBottom';
import Section from '../Section';
import Wireframe from '../../snippets/Wireframe/Wireframe';

const stories = storiesOf('Разметка/Страницы', module);

stories.add('Слева-справа (с шапкой)', () => {
  return (
    <div style={{ width: 300 }}>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-top" title="Header" />
        </div>
      </div>
      <LeftTopBottom>
        <Section place="left">
          <div style={{ height: 150 }}>
            <Wireframe title="Left" />
          </div>
        </Section>
        <Section place="top">
          <div style={{ height: 75, position: 'relative' }}>
            <Wireframe title="Top" />
          </div>
        </Section>
        <Section place="bottom">
          <div style={{ height: 75, position: 'relative' }}>
            <Wireframe title="Bottom" />
          </div>
        </Section>
      </LeftTopBottom>
      <div className="row">
        <div className="col-md-12 position-relative" style={{ height: 60 }}>
          <Wireframe className="disabled gradient-bottom" title="Footer" />
        </div>
      </div>
    </div>
  );
});
