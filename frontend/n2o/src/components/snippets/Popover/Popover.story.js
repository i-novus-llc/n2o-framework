import React from 'react';
import { storiesOf } from '@storybook/react';
import Popover from '../Popover/Popover';
import Button from 'reactstrap/es/Button';
import ButtonGroup from 'reactstrap/es/ButtonGroup';

const stories = storiesOf('UI Компоненты/Popover', module);

const props = {
  component: {
    body: 'Sed posuere consectetur est at lobortis. Aenean eu leo quam.',
    header: 'Popover Title',
  },
  placement: {
    body: 'Sed posuere consectetur est at lobortis. Aenean eu leo quam.',
    header: 'Popover Title',
    trigger: 'hover',
  },
  popConfirm: {
    header: 'Are you sure?',
    okText: 'Yes',
    cancelText: 'No',
  },
};

stories.add('Компонент', () => (
  <React.Fragment>
    <ButtonGroup className="col-md-12 justify-content-center">
      <Popover
        {...props.component}
        placement="top"
        className="row-md-2 mr-md-5"
      >
        <Button>Top</Button>
      </Popover>
      <Popover
        {...props.component}
        placement="bottom"
        className="row-md-2 mr-md-5"
      >
        <Button>Bottom</Button>
      </Popover>
      <Popover
        {...props.component}
        placement="left"
        className="row-md-2 mr-md-5"
      >
        <Button>Left</Button>
      </Popover>
      <Popover
        {...props.component}
        placement="right"
        className="row-md-2 mr-md-5"
      >
        <Button>Right</Button>
      </Popover>
    </ButtonGroup>
  </React.Fragment>
));

stories.add('Подсказка', () => (
  <React.Fragment>
    <ButtonGroup className="col-md-12 justify-content-center">
      <label>Click</label>
      <Popover
        placement="top"
        help="top"
        className="row-md-2 mr-md-5"
        trigger="click"
        iconClassName="m-md-1"
      />
      <label>Hover</label>
      <Popover
        placement="bottom"
        help="bottom"
        className="row-md-2 mr-md-5"
        trigger="hover"
        iconClassName="m-md-1"
      />
      <label>Focus</label>
      <Popover
        placement="left"
        help="left"
        className="row-md-2 mr-md-5"
        trigger="focus"
        iconClassName="m-md-1"
      />
      <label>Legacy</label>
      <Popover
        placement="right"
        help="right"
        className="row-md-2 mr-md-5"
        trigger="legacy"
        iconClassName="m-md-1"
      />
    </ButtonGroup>
  </React.Fragment>
));

stories.add('Placement', () => (
  <React.Fragment>
    <ButtonGroup className="col-md-12 justify-content-center">
      <Popover
        {...props.placement}
        placement="top-start"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Top-start</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="top"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Top</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="top-end"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Top-end</Button>
      </Popover>
    </ButtonGroup>
    <ButtonGroup className="btn-group-vertical col-md-6 align-items-center">
      <Popover
        {...props.placement}
        placement="left-start"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Left-start</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="left"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Left</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="left-end"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Left-end</Button>
      </Popover>
    </ButtonGroup>
    <ButtonGroup className="btn-group-vertical col-md-6 align-items-md-center">
      <Popover
        {...props.placement}
        placement="right-start"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Right-start</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="right"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Right</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="right-end"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Right-end</Button>
      </Popover>
    </ButtonGroup>
    <ButtonGroup className="col-md-12 justify-content-center">
      <Popover
        {...props.placement}
        placement="bottom-start"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Bottom-start</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="bottom"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Bottom</Button>
      </Popover>
      <Popover
        {...props.placement}
        placement="bottom-end"
        className="row-md-2 mr-md-5"
      >
        <Button className="mb-1">Bottom-end</Button>
      </Popover>
    </ButtonGroup>
  </React.Fragment>
));

stories.add('popConfirm', () => (
  <React.Fragment>
    <ButtonGroup className="col-md-12 justify-content-center">
      <Popover
        {...props.popConfirm}
        placement="top-start"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Top-start</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="top"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Top</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="top-end"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Top-end</Button>
      </Popover>
    </ButtonGroup>
    <ButtonGroup className="btn-group-vertical col-md-6 align-items-center">
      <Popover
        {...props.popConfirm}
        placement="left-start"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Left-start</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="left"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Left</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="left-end"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Left-end</Button>
      </Popover>
    </ButtonGroup>
    <ButtonGroup className="btn-group-vertical col-md-6 align-items-md-center">
      <Popover
        {...props.popConfirm}
        placement="right-start"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Right-start</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="right"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Right</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="right-end"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Right-end</Button>
      </Popover>
    </ButtonGroup>
    <ButtonGroup className="col-md-12 justify-content-center">
      <Popover
        {...props.popConfirm}
        placement="bottom-start"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Bottom-start</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="bottom"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Bottom</Button>
      </Popover>
      <Popover
        {...props.popConfirm}
        placement="bottom-end"
        className="row-md-2 mr-md-5"
        popConfirm={true}
      >
        <Button className="mb-1">Bottom-end</Button>
      </Popover>
    </ButtonGroup>
  </React.Fragment>
));
