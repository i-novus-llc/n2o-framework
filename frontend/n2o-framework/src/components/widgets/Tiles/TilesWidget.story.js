import React from 'react';
import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';

import metadata from './TilesWidget.meta.json';
import Tiles from './Tiles';
import TilesWidget from './TilesWidget';

const stories = storiesOf('Виджеты/Виджет Карточки', module);

stories.addParameters({
  info: {
    propTables: [Tiles],
    propTablesExclude: [TilesWidget],
  },
});

const urlPattern = '*';

stories.add('Компонент', () => {
  fetchMock.restore().get(urlPattern, url => {
    return {
      meta: {},
      page: 0,
      size: 10,
      list: [
        {
          tile1: {
            title: 'Ячейка с картинкой',
            url:
              'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ',
          },
          tile2: {
            text: 'Карточка 1',
          },
        },
        {
          tile1: {
            title: 'Ячейка с картинкой 2',
            url:
              'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ',
          },
          tile2: {
            text: 'Карточка 2',
          },
        },
        {
          tile1: {
            title: 'Ячейка с картинкой 3',
            url:
              'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTivmPCOe7GabP3hXYjSilJL1rkyrKF65PLV-oFw4MsOagkurzQ',
          },
          tile2: {
            text: 'Карточка 3',
          },
        },
      ],
    };
  });

  const withDataProvider = {
    dataProvider: {
      url: 'n2o/data/test',
      pathMapping: {},
      queryMapping: {
        'filter.name': {
          link: "models.filter['Page_Table'].name",
        },
        'filter.surname': {
          link: "models.filter['Page_Table'].surname",
        },
      },
    },
    paging: {
      size: 10,
    },
    ...metadata['Page_Tiles'],
  };

  return <TilesWidget {...withDataProvider} id="Page_Tiles" />;
});
