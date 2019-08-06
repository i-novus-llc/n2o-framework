import React from 'react';
import { storiesOf } from '@storybook/react';
import Actions from '../Actions';

const stories = storiesOf('Действия/PopoverConfirm', module);

stories.add('Компонент', () => {
  return (
    <div className="col-md-12 d-flex justify-content-center">
      <Actions
        actions={{
          dummy: {
            src: 'dummyImpl',
          },
        }}
        toolbar={[
          {
            buttons: [
              {
                id: 'test',
                title: 'Кнопка с confirm',
                actionId: 'dummy',
                popoverConfirm: true,
                header: 'Вы уверены?',
                okText: 'Да',
                cancelText: 'Нет',
              },
            ],
          },
        ]}
        containerKey="test"
      />
    </div>
  );
});
