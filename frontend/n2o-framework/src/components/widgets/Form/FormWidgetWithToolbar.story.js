import React from 'react';
import { storiesOf } from '@storybook/react';

import { getStubData } from 'N2oStorybook/fetchMock';

import FormWidget from './FormWidget.meta.json';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';

const stories = storiesOf('Виджеты/Форма', module);

stories.add('Форма с toolbar', () => {
  const form = {
    fieldsets: [
      {
        src: 'StandardFieldset',
        rows: [
          {
            cols: [
              {
                size: 6,
                fields: [
                  {
                    id: 'name',
                    src: 'StandardField',
                    label: 'Поле 1',
                    control: {
                      src: 'InputText',
                      readOnly: false,
                    },
                    toolbar: {
                      topLeft: [
                        {
                          buttons: [
                            {
                              src: 'StandardButton',
                              id: 'update',
                              label: 'button 1',
                              icon: 'fa fa-trash',
                              actionId: 'update',
                              validate: true,
                              validatedWidgetId: 'create2_main',
                              color: 'primary',
                              hint: 'some hint',
                              size: 'sm',
                            },
                          ],
                        },
                      ],
                    },
                  },
                ],
              },
              {
                size: 6,
                fields: [
                  {
                    id: 'name',
                    src: 'StandardField',
                    label: 'Поле 2',
                    control: {
                      src: 'InputText',
                      readOnly: false,
                    },
                    toolbar: {
                      topLeft: [
                        {
                          buttons: [
                            {
                              src: 'DropdownButton',
                              id: 'update',
                              label: 'button 1',
                              actionId: 'update',
                              validate: true,
                              validatedWidgetId: 'create2_main',
                              color: 'secondary',
                              subMenu: [
                                {
                                  id: 'testBtn221',
                                  actionId: 'dummy',
                                  src: 'StandardButton',
                                  label: 'label',
                                  icon: 'fa fa-paper-plane',
                                  hint: 'Подписать запись',
                                  count: '4',
                                  size: 'sm',
                                  color: 'secondary',
                                  visible: true,
                                  disabled: false,
                                },
                              ],
                            },
                            {
                              src: 'DropdownButton',
                              id: 'update',
                              label: 'button 2',
                              actionId: 'update',
                              validate: true,
                              validatedWidgetId: 'create2_main',
                              color: 'secondary',
                              subMenu: [
                                {
                                  id: 'testBtn221',
                                  actionId: 'dummy',
                                  src: 'StandardButton',
                                  label: 'label',
                                  icon: 'fa fa-paper-plane',
                                  hint: 'Подписать запись',
                                  count: '4',
                                  size: 'sm',
                                  color: 'secondary',
                                  visible: true,
                                  disabled: false,
                                },
                              ],
                            },
                          ],
                        },
                      ],
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
    ],
  };

  return (
    <div>
      <Factory
        level={WIDGETS}
        {...FormWidget['Page_Form']}
        id="Page_Form"
        form={form}
        toolbar={{}}
      />
    </div>
  );
});
