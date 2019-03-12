import React from 'react';
import { storiesOf } from '@storybook/react';
import { pick } from 'lodash';
import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, newEntry, tableActions } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import Factory from '../../../core/factory/Factory';
import List from './List';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import withPage from '../../../../.storybook/decorators/withPage';
import { page } from 'N2oStorybook/fetchMock';
import metadata from './List.meta';
import customRowClick from '../AdvancedTable/json/CustomRowClick.meta';

const stories = storiesOf('Виджеты/Лист', module);

const urlPattern = 'begin:n2o/data';
const delay = ms => new Promise(r => setTimeout(() => r(), ms));
stories
  .addDecorator(withPage(metadata))
  .add('Компонент со стандартной реализацией', () => {
    const data = [
      {
        image: {
          src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg'
        },
        header: "It's a cat",
        subHeader: 'The cat is stupid',
        body: 'Some words about cats',
        rightTop: 'What do you know about cats?',
        rightBottom: "But cats aren't only stupid they're still so sweet",
        extra: 'Extra?!'
      },
      {
        image: {
          src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg'
        },
        header: "It's a cat",
        subHeader: 'The cat is stupid',
        body: 'Some words about cats',
        rightTop: 'What do you know about cats?',
        rightBottom: "But cats aren't only stupid they're still so sweet",
        extra: 'Extra?!'
      }
    ];

    return <List data={data} />;
  })
  .add('Компонент без разделителя строк', () => {
    const data = [
      {
        image: {
          src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg'
        },
        header: "It's a cat",
        subHeader: 'The cat is stupid',
        body: 'Some words about cats',
        rightTop: 'What do you know about cats?',
        rightBottom: "But cats aren't only stupid they're still so sweet",
        extra: 'Extra?!'
      },
      {
        image: {
          src: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg'
        },
        header: "It's a cat",
        subHeader: 'The cat is stupid',
        body: 'Some words about cats',
        rightTop: 'What do you know about cats?',
        rightBottom: "But cats aren't only stupid they're still so sweet",
        extra: 'Extra?!'
      }
    ];

    return <List data={data} divider={false} />;
  })
  .add('Метаданные с cells', () => {
    let data = [];
    for (let i = 0; i < 3; i++) {
      data.push({
        image: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
        header: "It's a cat",
        subHeader: 'The cat is stupid',
        body: 'Some words about cats',
        rightTop: 'What do you know about cats?',
        rightBottom: "But cats aren't only stupid they're still so sweet",
        extra: 'Extra?!'
      });
    }
    fetchMock.restore().get(urlPattern, url => {
      return {
        ...getStubData(url),
        list: data
      };
    });

    return <Factory level={WIDGETS} {...metadata['List']} hasMoreNutton={true} id="List" />;
  })
  .add('Кастомный клик по строке', () => {
    fetchMock.restore().get(urlPattern, url => ({
      list: [
        {
          image: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
          header: "It's a cat",
          subHeader: 'The cat is stupid',
          body: 'Some words about cats',
          rightTop: 'What do you know about cats?',
          rightBottom: "But cats aren't only stupid they're still so sweet",
          extra: 'Extra?!'
        },
        {
          image: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
          header: "It's a cat",
          subHeader: 'The cat is stupid',
          body: 'Some words about cats',
          rightTop: 'What do you know about cats?',
          rightBottom: "But cats aren't only stupid they're still so sweet",
          extra: 'Extra?!'
        }
      ]
    }));
    fetchMock.get('begin:n2o/page', page);
    const rowClick = {
      rowClick: {
        src: 'perform',
        options: {
          type: 'n2o/modals/INSERT',
          payload: {
            pageUrl: '/Uid',
            size: 'sm',
            visible: true,
            closeButton: true,
            title: 'Новое модальное окно',
            pageId: 'Uid'
          }
        }
      }
    };
    const props = pick({ ...metadata['List'] }, ['src', 'list', 'dataProvider']);
    return <Factory level={WIDGETS} {...props} hasMoreButton={false} {...rowClick} id="List" />;
  })
  .add('Кнопка "Еще"', () => {
    fetchMock.restore().get(urlPattern, url =>
      delay(1000).then(() => ({
        list: [
          {
            image: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
            header: "It's a cat",
            subHeader: 'The cat is stupid',
            body: 'Some words about cats',
            rightTop: 'What do you know about cats?',
            rightBottom: "But cats aren't only stupid they're still so sweet",
            extra: 'Extra?!'
          },
          {
            image: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
            header: "It's a cat",
            subHeader: 'The cat is stupid',
            body: 'Some words about cats',
            rightTop: 'What do you know about cats?',
            rightBottom: "But cats aren't only stupid they're still so sweet",
            extra: 'Extra?!'
          }
        ]
      }))
    );
    const props = pick({ ...metadata['List'] }, [
      'src',
      'list',
      'dataProvider',
      'paging',
      'hasMoreButton'
    ]);
    return <Factory level={WIDGETS} {...props} id="List" />;
  })
  .add('Скролл в компоненте', () => {
    let data = [];
    for (let i = 0; i < 10; i++) {
      data.push({
        image: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
        header: "It's a cat",
        subHeader: 'The cat is stupid',
        body: 'Some words about cats',
        rightTop: 'What do you know about cats?',
        rightBottom: "But cats aren't only stupid they're still so sweet",
        extra: 'Extra?!'
      });
    }
    fetchMock.restore().get(urlPattern, url =>
      delay(1000).then(() => {
        return {
          list: data
        };
      })
    );
    const props = pick({ ...metadata['List'] }, [
      'src',
      'list',
      'dataProvider',
      'paging',
      'fetchOnScroll'
    ]);
    return <Factory level={WIDGETS} maxHeight={290} {...props} fetchOnScroll={true} id="List" />;
  })
  .add('Компонент с paging', () => {
    let data = [];
    for (let i = 0; i < 5; i++) {
      data.push({
        image: 'https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg',
        header: "It's a cat",
        subHeader: 'The cat is stupid',
        body: 'Some words about cats',
        rightTop: 'What do you know about cats?',
        rightBottom: "But cats aren't only stupid they're still so sweet",
        extra: 'Extra?!'
      });
    }
    fetchMock.restore().get(urlPattern, url =>
      delay(1000).then(() => {
        return {
          ...getStubData(url),
          list: data
        };
      })
    );
    const props = pick({ ...metadata['List'] }, ['src', 'list', 'dataProvider', 'paging']);
    return <Factory level={WIDGETS} {...props} showPagination={true} id="List" />;
  });
