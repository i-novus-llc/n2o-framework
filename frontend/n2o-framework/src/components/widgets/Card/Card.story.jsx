import React, { Fragment } from 'react'
import CardLink from 'reactstrap/lib/CardLink'
import Row from 'reactstrap/lib/Row'
import Col from 'reactstrap/lib/Col'
import { storiesOf } from '@storybook/react'

import Toolbar from '../../buttons/Toolbar'
import Factory from '../../../core/factory/Factory'

import Card from './Card'
import { items } from './Card.meta'
import { CardItem } from './CardItem'

const stories = storiesOf('Виджеты/Карточка')

stories.addParameters({
    info: {
        propTables: [Card, CardItem],
        propTablesExclude: [Factory, Card.Item],
    },
})
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
]

const colors = ['primary', 'success', 'info', 'warning', 'danger']

stories
    .add(
        'Компонент',
        () => {
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
            }

            return <Card.Item {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Карточка'
      
      ~~~js
      import Card from 'n2o-framework/lib/components/widgets/Card/Card';
      
      <Card.Item
          header="Header"
          meta="Subtitle"
          text="Text"
          image="https://placeholdit.imgix.net/~text?txtsize=33&txt=318%C3%97180&w=318&h=180"
          extra="Extra"
      />
      ~~~
      `,
            },
        },
    )

    .add(
        'Метаданные',
        () => {
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
            }

            return <Card.Item {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Карточка'
      
      ~~~js
      import Card from 'n2o-framework/lib/components/widgets/Card/Card';
      
      <Card.Item
          header="Осипова Светлана"
          meta="Программист"
          text="Debian GNU/Linux заслуженно считается одним из лучших и наиболее популярных дистрибутивов..."
          image="https://upload.wikimedia.org/wikipedia/commons/2/2e/Not_boyfriends_computer.jpg"
      />
      ~~~
      `,
            },
        },
    )

    .add(
        'Группировка блоками',
        () => <Card items={items} linear={false} circle />,
        {
            info: {
                text: `
      Компонент 'Карточка'
      ~~~js
      import Card from 'n2o-framework/lib/components/widgets/Card/Card';
      
      <Card 
          circle={true}
          items={
            [
              {
                "header": "Никонова Юлия",
                "meta": "Дизайнер",
                "text": "Хипстерская тема очень популярна в дизайне, и веб-дизайне в частности. Красивая типографика, выдержанный минимализм...",
                "image": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ"
              },
              {
                "header": "Осипова Светлана",
                "meta": "Программист",
                "text": "Debian GNU/Linux заслуженно считается одним из лучших и наиболее популярных дистрибутивов...",
                "image": "https://upload.wikimedia.org/wikipedia/commons/2/2e/Not_boyfriends_computer.jpg",
                "rows": ["image", "header", "meta", "text", "extra"]
              },
              {
                "header": "Никонова Юлия",
                "meta": "Фотограф",
                "text": "Дискретность, мимолетность, мгновенность современного снимка. Пролистнули и забыли. Раньше было не так — снимков было меньше, они доставались дороже...",
                "image": "https://www.zastavki.com/pictures/1024x1024/2015/Girls_Smiling_beautiful_girl__photo_George_Chernyad_ev_111193_31.jpg"
              },
              {
                "header": "Иванова Вера",
                "meta": "Музыкант",
                "text": "Музыка для души – невероятная компиляция нежности, спокойствия и умиротворения с нотками приятной грусти. Это чувство сложно описать словами ...",
                "image": "https://c1.staticflickr.com/8/7574/29592416595_e471386275_b.jpg"
              },
              {
                "header": "Никонова Ольга",
                "meta": "Актриса",
                "text": "Быть или не быть, вот в чём вопрос. Достойно ль Смиряться под ударами судьбы...",
                "image": "http://sedayejavedan.persiangig.com/blue,girl,lonely,sad-a470b418368e548210d276093519d9ad_h.jpg"
              }
            ]
          }
      />
      ~~~
      `,
            },
        },
    )

    .add(
        'Группировка в линию',
        () => <Card items={items} linear circle={false} />,
        {
            info: {
                text: `
      Компонент 'Карточка'
      ~~~js
      import Card from 'n2o-framework/lib/components/widgets/Card/Card';
      
      <Card 
          linear={true}
          items={
            [
              {
                "header": "Никонова Юлия",
                "meta": "Дизайнер",
                "text": "Хипстерская тема очень популярна в дизайне, и веб-дизайне в частности. Красивая типографика, выдержанный минимализм...",
                "image": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ"
              },
              {
                "header": "Осипова Светлана",
                "meta": "Программист",
                "text": "Debian GNU/Linux заслуженно считается одним из лучших и наиболее популярных дистрибутивов...",
                "image": "https://upload.wikimedia.org/wikipedia/commons/2/2e/Not_boyfriends_computer.jpg",
                "rows": ["image", "header", "meta", "text", "extra"]
              },
              {
                "header": "Никонова Юлия",
                "meta": "Фотограф",
                "text": "Дискретность, мимолетность, мгновенность современного снимка. Пролистнули и забыли. Раньше было не так — снимков было меньше, они доставались дороже...",
                "image": "https://www.zastavki.com/pictures/1024x1024/2015/Girls_Smiling_beautiful_girl__photo_George_Chernyad_ev_111193_31.jpg"
              },
              {
                "header": "Иванова Вера",
                "meta": "Музыкант",
                "text": "Музыка для души – невероятная компиляция нежности, спокойствия и умиротворения с нотками приятной грусти. Это чувство сложно описать словами ...",
                "image": "https://c1.staticflickr.com/8/7574/29592416595_e471386275_b.jpg"
              },
              {
                "header": "Никонова Ольга",
                "meta": "Актриса",
                "text": "Быть или не быть, вот в чём вопрос. Достойно ль Смиряться под ударами судьбы...",
                "image": "http://sedayejavedan.persiangig.com/blue,girl,lonely,sad-a470b418368e548210d276093519d9ad_h.jpg"
              }
            ]
          }
      />
      ~~~
      `,
            },
        },
    )

    .add(
        'Расширение в блочном отображении',
        () => (
            <Card.Layout>
                <Card.Item
                    {...items[0]}
                    extra={(
                        <>
                            <CardLink href="#">Читать</CardLink>
                            <CardLink href="#">Профиль</CardLink>
                        </>
                    )}
                />
                <Card.Item {...items[1]} extra={<Toolbar toolbar={btnAct} />} />
                <Card.Item
                    {...items[2]}
                    rows={['extra', 'text', 'header', 'meta', 'image']}
                />
            </Card.Layout>
        ),
        {
            info: {
                text: `
      Компонент 'Карточка'
      ~~~js
      import Card from 'n2o-framework/lib/components/widgets/Card/Card';
      
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
        <Card.Item {...items[1]} extra={<Toolbar toolbar={btnAct} />} />
        <Card.Item
          {...items[2]}
          rows={['extra', 'text', 'header', 'meta', 'image']}
        />
      </Card.Layout>
      ~~~
      `,
            },
        },
    )

    .add(
        'Расширение в линейном отображении',
        () => {
            const props = items.map(i => ({ ...i, linear: true, circle: true }))

            return (
                <Card.Layout>
                    <Card.Item
                        {...props[0]}
                        extra={(
                            <>
                                <CardLink href="#">Читать</CardLink>
                                <CardLink href="#">Профиль</CardLink>
                            </>
                        )}
                    />
                    <Card.Item {...props[1]} extra={<Toolbar toolbar={btnAct} />} />
                    <Card.Item
                        {...props[2]}
                        extra={<Toolbar toolbar={[btnAct[0]]} />}
                        rows={['extra', 'header', 'text', 'image', 'meta']}
                    />
                </Card.Layout>
            )
        },
        {
            info: {
                text: `
      Компонент 'Карточка'
      ~~~js
      import Card from 'n2o-framework/lib/components/widgets/Card/Card';
      
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
        <Card.Item {...props[1]} extra={<Toolbar toolbar={btnAct} />} />
        <Card.Item
          {...props[2]}
          extra={<Toolbar toolbar={[btnAct[0]]} />}
          rows={['extra', 'header', 'text', 'image', 'meta']}
        />
      </Card.Layout>
      ~~~
      `,
            },
        },
    )
    .add(
        'Статусы',
        () => {
            const props = items.map((item, i) => ({
                ...item,
                color: colors[i],
            }))

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
            )
        },
        {
            info: {
                text: `
      Компонент 'Карточка'
      ~~~js
      import Card from 'n2o-framework/lib/components/widgets/Card/Card';
      
      const props = [
        {
          "header": "Никонова Юлия",
          "meta": "Дизайнер",
          "text": "Хипстерская тема очень популярна в дизайне, и веб-дизайне в частности. Красивая типографика, выдержанный минимализм...",
          "image": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ",
          "color": "primary"
        },
        {
          "header": "Осипова Светлана",
          "meta": "Программист",
          "text": "Debian GNU/Linux заслуженно считается одним из лучших и наиболее популярных дистрибутивов...",
          "image": "https://upload.wikimedia.org/wikipedia/commons/2/2e/Not_boyfriends_computer.jpg",
          "rows": ["image", "header", "meta", "text", "extra"],
          "color": "success"
        },
        {
          "header": "Никонова Юлия",
          "meta": "Фотограф",
          "text": "Дискретность, мимолетность, мгновенность современного снимка. Пролистнули и забыли. Раньше было не так — снимков было меньше, они доставались дороже...",
          "image": "https://www.zastavki.com/pictures/1024x1024/2015/Girls_Smiling_beautiful_girl__photo_George_Chernyad_ev_111193_31.jpg",
          "color": "info"
        },
        {
          "header": "Иванова Вера",
          "meta": "Музыкант",
          "text": "Музыка для души – невероятная компиляция нежности, спокойствия и умиротворения с нотками приятной грусти. Это чувство сложно описать словами ...",
          "image": "https://c1.staticflickr.com/8/7574/29592416595_e471386275_b.jpg",
          "color": "warning"
        },
        {
          "header": "Никонова Ольга",
          "meta": "Актриса",
          "text": "Быть или не быть, вот в чём вопрос. Достойно ль Смиряться под ударами судьбы...",
          "image": "http://sedayejavedan.persiangig.com/blue,girl,lonely,sad-a470b418368e548210d276093519d9ad_h.jpg",
          "color": "danger"
        }
      ];
      
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
      ~~~
      `,
            },
        },
    )
