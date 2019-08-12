import React, { Fragment } from 'react';
import { CardLink, Row, Col } from 'reactstrap';
import { storiesOf } from '@storybook/react';
import Card from './Card';
import Actions from '../../actions/Actions';
import { items } from './Card.meta';
import { CardItem } from './CardItem';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Виджеты/Карточка');

stories.addParameters({
  info: {
    propTables: [Card, CardItem],
    propTablesExclude: [Factory, Card.Item],
  },
});
const btnAct = [
  {
    buttons: [
      {
        id: 'testBtn13',
        actionId: 'dummy',
        icon: 'fa fa-apple',
      },
    ],
  },
  {
    buttons: [
      {
        id: 'testBtn14',
        actionId: 'dummy',
        icon: 'fa fa-github',
      },
    ],
  },
  {
    buttons: [
      {
        id: 'testBtn15',
        actionId: 'dummy',
        icon: 'fa fa-telegram',
      },
    ],
  },
];

const colors = ['primary', 'success', 'info', 'warning', 'danger'];

stories
  .add('Компонент', () => {
    const props = {
      header: 'Header',
      meta: 'Subtitle',
      text: 'Text',
      image:
        'https://placeholdit.imgix.net/~text?txtsize=33&txt=318%C3%97180&w=318&h=180',
      extra: 'Extra',
      linear: false,
      circle: false,
      color: [...colors, null],
      inverse: false,
      outline: false,
    };
    return <Card.Item {...props} />;
  })

  .add('Метаданные', () => {
    const props = {
      header: items[1].header,
      meta: items[1].meta,
      text: items[1].text,
      image: items[1].image,
      extra: items[1].extra,
      linear: items[1].linear,
      rows: items[1].rows,
      circle: items[1].circle,
      color: [...colors, null],
      inverse: false,
      outline: true,
    };

    return <Card.Item {...props} />;
  })

  .add('Группировка блоками', () => {
    return <Card items={items} linear={false} circle={true} />;
  })

  .add('Группировка в линию', () => {
    return <Card items={items} linear={true} circle={false} />;
  })

  .add('Расширение в блочном отображении', () => (
    <Card.Layout>
      <Card.Item
        {...items[0]}
        extra={
          <Fragment>
            <CardLink href="#">Читать</CardLink>
            <CardLink href="#">Профиль</CardLink>
          </Fragment>
        }
      />
      <Card.Item {...items[1]} extra={<Actions toolbar={btnAct} />} />
      <Card.Item
        {...items[2]}
        rows={['extra', 'text', 'header', 'meta', 'image']}
      />
    </Card.Layout>
  ))

  .add('Расширение в линейном отображении', () => {
    const props = items.map(i => ({ ...i, linear: true, circle: true }));

    return (
      <Card.Layout>
        <Card.Item
          {...props[0]}
          extra={
            <Fragment>
              <CardLink href="#">Читать</CardLink>
              <CardLink href="#">Профиль</CardLink>
            </Fragment>
          }
        />
        <Card.Item {...props[1]} extra={<Actions toolbar={btnAct} />} />
        <Card.Item
          {...props[2]}
          extra={<Actions toolbar={[btnAct[0]]} />}
          rows={['extra', 'header', 'text', 'image', 'meta']}
        />
      </Card.Layout>
    );
  })
  .add('Статусы', () => {
    const props = items.map((item, i) => ({
      ...item,
      color: colors[i],
    }));

    return (
      <Row>
        <Col lg={12}>
          <Card items={props} linear outline circle />
        </Col>
        <Col lg={12}>
          <Card items={props} linear circle inverse />
        </Col>
        <Col lg={12}>
          <Card items={props} circle outline />
        </Col>
        <Col lg={12}>
          <Card items={props} inverse />
        </Col>
      </Row>
    );
  });
