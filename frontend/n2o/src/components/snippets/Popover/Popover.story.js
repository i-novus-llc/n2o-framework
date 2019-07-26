import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import Popover from '../Popover/Popover';
import Button from 'reactstrap/es/Button';
import ButtonGroup from 'reactstrap/es/ButtonGroup';
import { text, boolean, withKnobs } from '@storybook/addon-knobs/dist/react';
import Factory from '../../../core/factory/Factory';
import { SNIPPETS } from '../../../core/factory/factoryLevels';

const stories = storiesOf('UI Компоненты/Popover', module);

const props = {
  component: {
    body: 'Sed posuere consectetur est at lobortis. Aenean eu leo quam.',
    header: 'Popover Title',
    className: 'row-md-2 mr-md-5',
  },
  help: {
    className: 'row-md-2 mr-md-5',
    iconClassName: 'm-md-1',
  },
  placement: {
    body: 'Sed posuere consectetur est at lobortis. Aenean eu leo quam.',
    header: 'Popover Title',
    trigger: 'hover',
    className: 'row-md-2 mr-md-5',
  },
  popConfirm: {
    header: 'Are you sure?',
    okText: 'Yes',
    cancelText: 'No',
    className: 'row-md-2 mr-md-5',
    popConfirm: true,
  },
};

stories
  .addDecorator(withKnobs)
  .add('Компонент', () => (
    <Fragment>
      <ButtonGroup className="col-md-12 justify-content-center">
        <Popover {...props.component} placement="top">
          <Button>Top</Button>
        </Popover>
        <Popover {...props.component} placement="bottom">
          <Button>Bottom</Button>
        </Popover>
        <Popover {...props.component} placement="left">
          <Button>Left</Button>
        </Popover>
        <Popover {...props.component} placement="right">
          <Button>Right</Button>
        </Popover>
      </ButtonGroup>
    </Fragment>
  ))

  .add('Подсказка', () => (
    <Fragment>
      <ButtonGroup className="col-md-12 justify-content-center">
        <label>Click</label>
        <Popover {...props.help} placement="top" help="top" trigger="click" />
        <label>Hover</label>
        <Popover
          {...props.help}
          placement="bottom"
          help="bottom"
          trigger="hover"
        />
        <label>Focus</label>
        <Popover {...props.help} placement="left" help="left" trigger="focus" />
        <label>Legacy</label>
        <Popover
          {...props.help}
          placement="right"
          help="right"
          trigger="legacy"
        />
      </ButtonGroup>
    </Fragment>
  ))

  .add('Placement', () => (
    <Fragment>
      <ButtonGroup className="col-md-12 justify-content-center">
        <Popover {...props.placement} placement="top-start">
          <Button className="mb-1">Top-start</Button>
        </Popover>
        <Popover {...props.placement} placement="top">
          <Button className="mb-1">Top</Button>
        </Popover>
        <Popover {...props.placement} placement="top-end">
          <Button className="mb-1">Top-end</Button>
        </Popover>
      </ButtonGroup>
      <ButtonGroup className="btn-group-vertical col-md-6 align-items-center">
        <Popover {...props.placement} placement="left-start">
          <Button className="mb-1">Left-start</Button>
        </Popover>
        <Popover {...props.placement} placement="left">
          <Button className="mb-1">Left</Button>
        </Popover>
        <Popover {...props.placement} placement="left-end">
          <Button className="mb-1">Left-end</Button>
        </Popover>
      </ButtonGroup>
      <ButtonGroup className="btn-group-vertical col-md-6 align-items-md-center">
        <Popover {...props.placement} placement="right-start">
          <Button className="mb-1">Right-start</Button>
        </Popover>
        <Popover {...props.placement} placement="right">
          <Button className="mb-1">Right</Button>
        </Popover>
        <Popover {...props.placement} placement="right-end">
          <Button className="mb-1">Right-end</Button>
        </Popover>
      </ButtonGroup>
      <ButtonGroup className="col-md-12 justify-content-center">
        <Popover {...props.placement} placement="bottom-start">
          <Button className="mb-1">Bottom-start</Button>
        </Popover>
        <Popover {...props.placement} placement="bottom">
          <Button className="mb-1">Bottom</Button>
        </Popover>
        <Popover {...props.placement} placement="bottom-end">
          <Button className="mb-1">Bottom-end</Button>
        </Popover>
      </ButtonGroup>
    </Fragment>
  ))

  .add('popConfirm', () => (
    <Fragment>
      <ButtonGroup className="col-md-12 justify-content-center">
        <Popover {...props.popConfirm} placement="top-start">
          <Button className="mb-1">Top-start</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="top">
          <Button className="mb-1">Top</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="top-end">
          <Button className="mb-1">Top-end</Button>
        </Popover>
      </ButtonGroup>
      <ButtonGroup className="btn-group-vertical col-md-6 align-items-center">
        <Popover {...props.popConfirm} placement="left-start">
          <Button className="mb-1">Left-start</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="left">
          <Button className="mb-1">Left</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="left-end">
          <Button className="mb-1">Left-end</Button>
        </Popover>
      </ButtonGroup>
      <ButtonGroup className="btn-group-vertical col-md-6 align-items-md-center">
        <Popover {...props.popConfirm} placement="right-start">
          <Button className="mb-1">Right-start</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="right">
          <Button className="mb-1">Right</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="right-end">
          <Button className="mb-1">Right-end</Button>
        </Popover>
      </ButtonGroup>
      <ButtonGroup className="col-md-12 justify-content-center">
        <Popover {...props.popConfirm} placement="bottom-start">
          <Button className="mb-1">Bottom-start</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="bottom">
          <Button className="mb-1">Bottom</Button>
        </Popover>
        <Popover {...props.popConfirm} placement="bottom-end">
          <Button className="mb-1">Bottom-end</Button>
        </Popover>
      </ButtonGroup>
    </Fragment>
  ))
  .add('Создание через Factory', () => {
    const dt = {
      id: 'popover',
      src: 'Popover',
      header: text('header', 'Header'),
      body: text('body', 'Body'),
      target: text('target', 'targetId'),
      popConfirm: boolean('popConfirm', false),
      okText: text('okText', 'Ok'),
      cancelText: text('cancelText', 'Cancel'),
    };
    return (
      <div className="col-md-12 d-flex justify-content-center">
        <Factory level={SNIPPETS} id={'popover'} {...dt} />
        <Button id="targetId">Factory Popover</Button>
      </div>
    );
  });
