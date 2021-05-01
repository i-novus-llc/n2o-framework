import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import PopoverConfirm from './PopoverConfirm';
import Button from 'reactstrap/lib/Button';
import ButtonGroup from 'reactstrap/lib/ButtonGroup';
import Factory from '../../../core/factory/Factory';
import { SNIPPETS } from '../../../core/factory/factoryLevels';

const stories = storiesOf('UI Компоненты/PopoverConfirm', module);

const props = {
  className: 'row-md-2 mr-md-5',
};

stories
  .add('Компонент', () => (
    <Fragment>
      <ButtonGroup className="col-md-12 justify-content-center">
        <PopoverConfirm {...props} placement="top">
          <Button>Top</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="bottom">
          <Button>Bottom</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="left">
          <Button>Left</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="right">
          <Button>Right</Button>
        </PopoverConfirm>
      </ButtonGroup>
    </Fragment>
  ))

  .add('Placement', () => (
    <Fragment>
      <ButtonGroup className="col-md-12 justify-content-center">
        <PopoverConfirm {...props} placement="top-start">
          <Button className="mb-1">Top-start</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="top">
          <Button className="mb-1">Top</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="top-end">
          <Button className="mb-1">Top-end</Button>
        </PopoverConfirm>
      </ButtonGroup>
      <ButtonGroup className="btn-group-vertical col-md-6 align-items-center">
        <PopoverConfirm {...props} placement="left-start">
          <Button className="mb-1">Left-start</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="left">
          <Button className="mb-1">Left</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="left-end">
          <Button className="mb-1">Left-end</Button>
        </PopoverConfirm>
      </ButtonGroup>
      <ButtonGroup className="btn-group-vertical col-md-6 align-items-md-center">
        <PopoverConfirm {...props} placement="right-start">
          <Button className="mb-1">Right-start</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="right">
          <Button className="mb-1">Right</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="right-end">
          <Button className="mb-1">Right-end</Button>
        </PopoverConfirm>
      </ButtonGroup>
      <ButtonGroup className="col-md-12 justify-content-center">
        <PopoverConfirm {...props} placement="bottom-start">
          <Button className="mb-1">Bottom-start</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="bottom">
          <Button className="mb-1">Bottom</Button>
        </PopoverConfirm>
        <PopoverConfirm {...props} placement="bottom-end">
          <Button className="mb-1">Bottom-end</Button>
        </PopoverConfirm>
      </ButtonGroup>
    </Fragment>
  ))

  .add('Создание через Factory', () => {
    const dt = {
      id: 'PopoverConfirm',
      src: 'PopoverConfirm',
      header: 'Header',
      target: 'targetId',
      okText: 'Ok',
      cancelText: 'Cancel',
    };
    return (
      <div className="col-md-12 d-flex justify-content-center">
        <Factory level={SNIPPETS} {...dt} />
        <Button id="targetId">Factory Popover</Button>
      </div>
    );
  });
