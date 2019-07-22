import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import {
  withKnobs,
  text,
  boolean,
  number,
  array,
  select,
} from '@storybook/addon-knobs/react';
import { withState } from '@dump247/storybook-state';
import Popover from '../Popover/Popover';
import Button from 'reactstrap/es/Button';
import ButtonGroup from 'reactstrap/es/ButtonGroup';

const stories = storiesOf('UI Компоненты/Popover', module);

// stories.addDecorator(withKnobs);

const props = {
  isOpen: false,
  body: 'Sed posuere consectetur est at lobortis. Aenean eu leo quam.',
  header: 'Popover Title',
};

stories.add('Компонент', () => (
  <React.Fragment>
    <ButtonGroup>
      <Popover {...props} placement="top" className="row-md-2 mr-md-5">
        <Button>Top</Button>
      </Popover>
      <Popover {...props} placement="bottom" className="row-md-2 mr-md-5">
        <Button>Bottom</Button>
      </Popover>
      <Popover {...props} placement="left" className="row-md-2 mr-md-5">
        <Button>Left</Button>
      </Popover>
      <Popover {...props} placement="right" className="row-md-2 mr-md-5">
        <Button>Right</Button>
      </Popover>
    </ButtonGroup>
  </React.Fragment>
));

stories.add('help', () => (
  <React.Fragment>
    <ButtonGroup>
      <label id="4">Top</label>
      <Popover
        {...props}
        placement="top"
        help="top"
        className="row-md-2 mr-md-5"
        target="4"
      />
      <label id="2">Bottom</label>
      <Popover
        {...props}
        placement="bottom"
        help="bottom"
        className="row-md-2 mr-md-5"
        target="2"
      />
      <label id="3">Left</label>
      <Popover
        {...props}
        placement="left"
        help="left"
        className="row-md-2 mr-md-5"
        target="3"
      />
      <label id="1">Right</label>
      <Popover
        {...props}
        placement="right"
        help="right"
        target="1"
        className="row-md-2 mr-md-5"
      />
    </ButtonGroup>
  </React.Fragment>
));
