import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, object } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { getStubData } from 'N2oStorybook/fetchMock';
import withPage from 'N2oStorybook/decorators/withPage';
import {
  FormFields,
  FormValidations,
  FieldLabelPosition,
  FormServerMessage,
  FormCollapseFieldset,
  FormFieldsetCollapseVE,
  FormFieldsetStandartVE
} from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import InputSelectContainerJson from '../../controls/InputSelect/InputSelectContainer.meta';

import FormWidgetData from './FormWidget.meta.json';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';

const stories = storiesOf('Виджеты/Форма', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Form'));

const renderForm = json => <Factory level={WIDGETS} {...json['Page_Form']} id="Page_Form" />;

stories
  .addDecorator(story => {
    fetchMock.restore().getOnce('begin:n2o/data', getStubData);
    return story();
  })
  .add('Метаданные', () => withPage(FormWidgetData)(() => renderForm(FormWidgetData)))
  .add('Расположение лейбла', () =>
    withPage(FieldLabelPosition)(() => renderForm(FieldLabelPosition))
  )
  .add('Экшены полей', () => withPage(FieldLabelPosition)(() => renderForm(FormFields)))
  .add('Отображение в полях сообщений от сервера', () =>
    withPage(FormServerMessage)(() => {
      fetchMock.restore().post('begin:n2o/data', url => ({
        status: 500,
        body: {
          meta: {
            messages: {
              form: 'Page_Form',
              fields: {
                name: {
                  text: 'Ошибка',
                  severity: 'danger'
                },
                surname: {
                  text: 'Предупреждение',
                  severity: 'warning'
                },
                age: {
                  text: 'Успех',
                  severity: 'success'
                }
              }
            }
          }
        }
      }));

      return renderForm(FormServerMessage);
    })
  )
  .add('Валидации', () =>
    withPage(FormValidations)(() => {
      const mockJson = {
        status: 200,
        sendAsJson: true,
        body: {
          message: [
            {
              severity: 'success',
              text: 'Доступная фамилия'
            },
            {
              severity: 'danger',
              text: 'Был отправлен запрос, получен ответ с ошибкой'
            }
          ]
        }
      };

      fetchMock.restore().get('begin:n2o/validation', mockJson);

      return renderForm(FormValidations);
    })
  )
  .add('Сворачиваемая форма', () =>
    withPage(FormCollapseFieldset)(() => {
      return renderForm(FormCollapseFieldset);
    })
  )
  .add('Видимость и блокировка филдсета', () =>
    withPage(FormFieldsetStandartVE)(() => {
      return renderForm(FormFieldsetStandartVE);
    })
  );
