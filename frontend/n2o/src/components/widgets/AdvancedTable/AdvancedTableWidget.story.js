import React from 'react';
import { get, set } from 'lodash';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, newEntry } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import metadata from './json/AdvancedTableWidget.meta';
import resizable from './json/Resizable.meta';
import rowSelection from './json/RowSelection.meta';
import filterable from './json/Filterable.meta';
import expandedRow from './json/ExpandedRow.meta';
import colSpanRowSpan from './json/ColspanRowspan.meta';
import treeView from './json/TreeView.meta';
import fixedHeader from './json/FixedHeader.meta';
import fixedColumns from './json/FixedColumns.meta';
import multiLevelHeader from './json/MultiLevelHeader.meta';
import editableCell from './json/EditableCell.meta';
import nested from './json/Nested.meta';
import customRowClick from './json/CustomRowClick.meta';
import allFeatures from './json/AllFeatures.meta';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import withPage from '../../../../.storybook/decorators/withPage';
import { page } from 'N2oStorybook/fetchMock';
import AdvancedTable from './AdvancedTable';
import CheckboxN2O from '../../controls/Checkbox/CheckboxN2O';
import percentWidth from './json/PercentWidth.meta';
import pixelWidth from './json/PixelWidth.meta';

const stories = storiesOf('Виджеты/Advanced Table', module);

const urlPattern = 'begin:n2o/data';

class AdvancedTableWidgetStory extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      ...props.json,
      filterable: get(props.json, 'columns[1].filterable'),
      resizable: get(props.json, 'columns[1].resizable'),
      fixedLeft: get(props.json, 'columns[0].fixed'),
      fixedRight: get(props.json, 'columns[3].fixed'),
    };

    this.toggleHeaderParam = this.toggleHeaderParam.bind(this);
    this.toggleFixed = this.toggleFixed.bind(this);
    this.toggleScroll = this.toggleScroll.bind(this);
  }

  toggleHeaderParam(name) {
    let columns = this.state.columns;
    columns[1][name] = !this.state[name];
    this.setState({
      columns,
      [name]: !this.state[name],
    });
  }

  toggleFixed(name, index) {
    let columns = this.state.columns;
    let newDirection = null;
    if (name === 'fixedLeft') {
      newDirection = this.state[name] ? false : 'left';
    } else {
      newDirection = this.state[name] ? false : 'right';
    }
    columns[index].fixed = newDirection;
    this.setState({
      columns,
      [name]: newDirection,
    });
  }

  toggleScroll(name) {
    let newScroll = this.state.scroll;
    if (name === 'x') {
      newScroll = this.state.scroll.x ? false : '200%';
    } else {
      newScroll = this.state.scroll.y ? false : 200;
    }
    this.setState({
      scroll: {
        ...this.state.scroll,
        [name]: newScroll,
      },
    });
  }

  render() {
    return (
      <div>
        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            justifyContent: 'space-between',
            marginBottom: 20,
          }}
        >
          <CheckboxN2O
            checked={this.state.resizable}
            onChange={() => this.toggleHeaderParam('resizable')}
            inline={true}
            label={'Функция resizable'}
          />
          <CheckboxN2O
            checked={this.state.filterable}
            onChange={() => this.toggleHeaderParam('filterable')}
            inline={true}
            label={'Функция filterable'}
          />
          <CheckboxN2O
            checked={this.state.rowSelection}
            onChange={() =>
              this.setState({ rowSelection: !this.state.rowSelection })
            }
            inline={true}
            label={'Функция выбора строк'}
          />
          <CheckboxN2O
            checked={this.state.fixedLeft}
            onChange={() => this.toggleFixed('fixedLeft', 0)}
            inline={true}
            label={'Фиксирование первой колонки слева'}
          />
          <CheckboxN2O
            checked={this.state.fixedRight}
            onChange={() => this.toggleFixed('fixedRight', 3)}
            inline={true}
            label={'Фиксирование последней колонки справа'}
          />
          <CheckboxN2O
            checked={this.state.scroll.x}
            onChange={() => this.toggleScroll('x')}
            inline={true}
            label={'Скролл-x'}
          />
          <CheckboxN2O
            checked={this.state.scroll.y}
            onChange={() => this.toggleScroll('y')}
            inline={true}
            label={'Скролл-y'}
          />
        </div>
        <AdvancedTable {...this.state} rowKey={record => record.id} />
      </div>
    );
  }
}

stories
  // .addDecorator(withPage(metadata))
  .addDecorator(jsxDecorator)
  .add('Метаданные', () => {
    fetchMock.restore().get(urlPattern, url => {
      console.log('point');
      console.log(url);
      return getStubData(url);
    });
    return (
      <Factory level={WIDGETS} {...metadata['Page_Table']} id="Page_Table" />
    );
  })
  .add('Resizable колонки', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    return (
      <Factory level={WIDGETS} {...resizable['Page_Table']} id="Page_Table" />
    );
  })
  .add('Выбор строк чекбоксом', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    return (
      <Factory
        level={WIDGETS}
        {...rowSelection['Page_Table']}
        id="Page_Table"
      />
    );
  })
  .add('Фильтр в заголовках', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    return (
      <Factory level={WIDGETS} {...filterable['Page_Table']} id="Page_Table" />
    );
  })
  .add('Контент в подстроке', () => {
    fetchMock.restore().get(urlPattern, () => {
      return {
        count: 3,
        filterValue: null,
        page: 1,
        size: 10,
        list: expandedRow.datasource,
      };
    });
    return (
      <Factory level={WIDGETS} {...expandedRow['Page_Table']} id="Page_Table" />
    );
  })
  .add('Colspan rowspan', () => {
    fetchMock.restore().get(urlPattern, () => ({
      count: 3,
      list: colSpanRowSpan.datasource,
      page: 1,
      size: 10,
    }));

    return (
      <Factory
        level={WIDGETS}
        {...colSpanRowSpan['Page_Table']}
        id="Page_Table"
      />
    );
  })
  .add('Вид дерево', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: treeView.datasource,
      };
    });

    return (
      <Factory level={WIDGETS} {...treeView['Page_Table']} id="Page_Table" />
    );
  })
  .add('Фиксированный заголовок', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    return (
      <Factory level={WIDGETS} {...fixedHeader['Page_Table']} id="Page_Table" />
    );
  })
  .add('Фиксированные колонки', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    return (
      <Factory
        level={WIDGETS}
        {...fixedColumns['Page_Table']}
        id="Page_Table"
      />
    );
  })
  .add('Многоуровневый заголовок', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    return (
      <Factory
        level={WIDGETS}
        {...multiLevelHeader['Page_Table']}
        id="Page_Table"
      />
    );
  })
  .add('Редактируемая ячейка', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    return (
      <Factory
        level={WIDGETS}
        {...editableCell['Page_Table']}
        id="Page_Table"
      />
    );
  })
  .add('Подтаблица', () => {
    fetchMock.restore().get(urlPattern, () => ({
      count: 1,
      page: 1,
      size: 10,
      list: nested.datasource,
    }));
    return (
      <Factory level={WIDGETS} {...nested['Page_Table']} id="Page_Table" />
    );
  })
  .add('Компонент с кастомным Expanded компонентом', () => {
    const props = {
      expandable: true,
      expandedFieldId: 'expandedContent',
      columns: [
        {
          id: 'test',
          key: 'test',
          dataIndex: 'test',
          title: 'test',
        },
        {
          id: 'anotherTest',
          key: 'anotherTest',
          dataIndex: 'anotherTest',
          title: 'anotherTest',
        },
      ],
      data: [
        {
          test: 'test1',
          anotherTest: 'anotherTest1',
        },
      ],
    };

    const expandedComponent = () => <div>any custom content</div>;

    return <AdvancedTable {...props} expandedComponent={expandedComponent} />;
  })
  .add('Экшен AdvancedTable', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    fetchMock.get('begin:n2o/page', page);
    return (
      <Factory
        level={WIDGETS}
        {...customRowClick['Page_Table']}
        id="Page_Table"
      />
    );
  })
  .add('Компонент со всеми фичами', () => {
    return <AdvancedTableWidgetStory json={allFeatures} />;
  })
  .add('Placeholder', () => {
    fetchMock.restore().get(urlPattern, url => {
      return new Promise((res, rej) =>
        setTimeout(() => res(getStubData(url)), 3000)
      );
    });

    const newMeta = { ...metadata };
    newMeta['Page_Table'].placeholder = { rows: 6, cols: 3, type: 'table' };

    return (
      <Factory level={WIDGETS} {...newMeta['Page_Table']} id="Page_Table" />
    );
  })
  .add('Колонки с длиной в процентах', () => {
    fetchMock.restore().get(urlPattern, url => {
      return new Promise((res, rej) =>
        setTimeout(() => res(getStubData(url)), 3000)
      );
    });

    return (
      <Factory
        level={WIDGETS}
        {...percentWidth['Page_Table']}
        id="Page_Table"
      />
    );
  })
  .add('Колонки с длиной в пикселях', () => {
    fetchMock.restore().get(urlPattern, url => {
      return new Promise((res, rej) =>
        setTimeout(() => res(getStubData(url)), 3000)
      );
    });

    return (
      <Factory level={WIDGETS} {...pixelWidth['Page_Table']} id="Page_Table" />
    );
  });
