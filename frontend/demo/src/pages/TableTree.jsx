import React from 'react';
import { Container, Row, Col } from 'reactstrap';

import { HeaderFooterTemplate } from 'n2o-framework/lib/components/core/templates';

import TableTree from '../components/widgets/TableTree/TableTree';
import staticData from '../components/widgets/TableTree/data.json';

class TableTreePage extends React.Component {
  constructor(props) {
    super(props);

    this.getItemsData = this.getItemsData.bind(this);
  }

  fetchRoots() {
    return staticData.children;
  }

  fetchChildrenOf(node) {
    return node.children;
  }

  getItemsData(parent) {
    return parent ? this.fetchChildrenOf(parent) : this.fetchRoots();
  }


  /**
   * Рендер
   */

  render() {
    const headers = [
      {
        title: 'Title',
        width: 300
      },
      {
        title: 'Number',
        width: 100
      },
      {
        title: 'Page',
        width: 100
      },
    ];

    return (
      <HeaderFooterTemplate>
        <Container>
          <Row>
            <Col md={8} mdOffset={3} style={{
              marginTop: '50px'
            }}>
              <p className="lead">Пример древовидной таблицы на основе <code>@atlaskit/table-tree</code></p>
              <p><small>Это кастомная страница внутри шаблона приложения</small></p>
              <TableTree
                headers={headers}
                items={this.getItemsData}
              />
            </Col>
          </Row>
        </Container>
      </HeaderFooterTemplate>
    );
  }
}

export default TableTreePage;
