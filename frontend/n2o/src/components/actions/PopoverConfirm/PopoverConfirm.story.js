import React from 'react';
import { storiesOf } from '@storybook/react';
import Actions from '../Actions';
import MetaJson from './PopoverConfirm.meta';

const stories = storiesOf('Действия/PopoverConfirm', module);

stories.add('Компонент', () => {
  return (
    <div className="col-md-12 d-flex justify-content-center">
      <Actions actions={MetaJson.actions} toolbar={MetaJson.toolbar} />
    </div>
  );
});
