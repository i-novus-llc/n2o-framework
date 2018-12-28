import React from 'react';
import { storiesOf } from '@storybook/react';

import Pagination from './Pagination';

const stories = storiesOf('UI Компоненты/Пагинация', module);

stories.add('Интерактивное использование', () => {
  class PaginationContainer extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        activePage: 1
      };
      this.handleChangePage = this.handleChangePage.bind(this);
    }

    handleChangePage(newPageIndex) {
      this.setState({
        activePage: newPageIndex
      });
    }

    render() {
      return (
        <Pagination
          onSelect={this.handleChangePage}
          activePage={this.state.activePage}
          size={10}
          count={100}
          maxButtons={4}
          stepIncrement={10}
        />
      );
    }
  }

  return <PaginationContainer />;
});
