import React from 'react';
import { storiesOf } from '@storybook/react';
import withPage from 'N2oStorybook/decorators/withPage';
import { RangeField as RangeFieldJson } from 'N2oStorybook/json';
import RangeField from './RangeField';
import meta from './RangeField.meta';
import Factory from '../../../../../core/factory/Factory';
import { WIDGETS } from '../../../../../core/factory/factoryLevels';

const stories = storiesOf('Виджеты/Форма/Fields/RangeField');
const renderForm = json => (
  <Factory level={WIDGETS} {...json['Page_Form']} id="Page_Form" />
);
stories.add('Сворачиваемая форма', () =>
  withPage({
    Page_Form: {
      src: 'FormWidget',
      dependency: {},
      dataProvider: {
        url: 'n2o/data/test',
        pathMapping: {},
        queryMapping: {},
      },
      form: {
        fetchOnInit: false,
        validation: {},
        fieldsets: [
          {
            src: 'RangeFieldset',
            type: 'line',
            label: 'Заголовок филдсета',
            expand: true,
            hasArrow: true,
            rows: [
              {
                cols: [
                  {
                    fields: [
                      {
                        id: 'name',
                        src: 'RangeField',
                        label: 'Имя',
                        beginControl: {
                          src: 'InputNumber',
                        },
                        endControl: {
                          src: 'InputNumber',
                        },
                      },
                    ],
                  },
                ],
              },
            ],
          },
        ],
      },
    },
  })(() => {
    return renderForm({
      Page_Form: {
        src: 'FormWidget',
        dependency: {},
        dataProvider: {
          url: 'n2o/data/test',
          pathMapping: {},
          queryMapping: {},
        },
        form: {
          fetchOnInit: false,
          validation: {},
          fieldsets: [
            {
              src: 'RangeFieldset',
              type: 'line',
              label: 'Заголовок филдсета',
              expand: true,
              hasArrow: true,
              rows: [
                {
                  cols: [
                    {
                      fields: [
                        {
                          id: 'name',
                          src: 'RangeField',
                          label: 'Имя',
                          beginControl: {
                            src: 'InputNumber',
                          },
                          endControl: {
                            src: 'InputNumber',
                          },
                        },
                      ],
                    },
                  ],
                },
              ],
            },
          ],
        },
      },
    });
  })
);
