import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs } from '@storybook/addon-knobs/react';

import Drawer from './Drawer';

const stories = storiesOf('UI Компоненты/Drawer', module);

stories.addDecorator(withKnobs).add('Компонент', () => (
  <React.Fragment>
    <Drawer
      animation={true}
      width="200px"
      placement="left"
      title="title"
      footer="footer"
    >
      <h1>Drawer</h1>
    </Drawer>
  </React.Fragment>
));
