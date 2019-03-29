import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { page } from 'N2oStorybook/fetchMock';
import {
  ShowModalTitle,
  placeholderToConfirm,
  ModalPage,
  ShowModal,
} from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';

import ModalPages from './ModalPages';
import Actions from '../actions/Actions';
import Factory from '../../core/factory/Factory';
import { WIDGETS } from '../../core/factory/factoryLevels';
import withPage from '../../../.storybook/decorators/withPage';
import { ModalWindow } from './ModalPage';

const stories = storiesOf('Действия/Модальное окно', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('ModalPages'));
stories.addDecorator(withPage(ShowModalTitle));

stories
  .add('Компонент', () => {
    return (
      <ModalWindow
        loading={false}
        title={'Заголовок компонента'}
        src={'OutputText'}
        visible={true}
      />
    );
  })
  .add('Открытие модального окна', () => {
    fetchMock.restore().get('begin:n2o/page', page);

    return (
      <Factory level={WIDGETS} {...ShowModal['Page_Form']} id="Page_Form" />
    );
  })
  .add('Плейсхолдер для модального окна', () => {
    fetchMock.restore().get('begin:n2o/page', page);

    return <Factory level={WIDGETS} {...placeholderToConfirm} id="Page_Form" />;
  })
  .add('Прелоадер модального окна', () => {
    return <ModalWindow />;
  });
