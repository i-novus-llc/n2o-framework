import React from 'react';
import Row from 'reactstrap/lib/Row';
import Col from 'reactstrap/lib/Col';
import { storiesOf } from '@storybook/react';

import { getStubData } from 'N2oStorybook/fetchMock';
import { FetchDependency, VisibleDependency } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';

import Factory from './factory/Factory';
import { WIDGETS } from './factory/factoryLevels';
import withPage from '../../.storybook/decorators/withPage';

const stories = storiesOf(
  'Функциональность/Зависимости между виджетами',
  module
);

const renderMasterDetail = (json, description) => (
  <Row>
    <Col>
      <Row>
        <div className="alert alert-secondary">{description}</div>
      </Row>
      <Row>
        <Col>
          <Factory level={WIDGETS} {...json['Page_First']} id="Page_First" />
        </Col>
        <Col>
          <Factory level={WIDGETS} {...json['Page_Second']} id="Page_Second" />
        </Col>
      </Row>
    </Col>
  </Row>
);

stories
  .addDecorator(story => {
    fetchMock.restore().get('begin:n2o/data', url => getStubData(url));
    return story();
  })

  .add('Master / Detail', () =>
    withPage(FetchDependency)(() =>
      renderMasterDetail(
        FetchDependency,
        'Значения в правой таблице фильтруются в зависимости от выбранного значения в левой таблице'
      )
    )
  )
  .add('Скрытие', () =>
    withPage(VisibleDependency)(() =>
      renderMasterDetail(
        VisibleDependency,
        'Скроет правый виджет, если выбрать в левой таблице строку со значением пола "Мужской"'
      )
    )
  );
