package gui;

/**
 * RevisedAuthorView
 * A new AuthorView based on the new PGChairView
 * Has edit buttons acting on each paper in the table.
 * 
 * @author yongyuwang
 * @version 3
 * Based on code from PGChairView and SubPGChairView
 * by Roshun Jones and Danielle Tucker respectively.
 */

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import model.Author;
import model.Paper;
import model.User;

import controller.Controller;
import controller.RevisedAuthorViewController;

public class AuthorView extends JPanel {
  private static final long serialVersionUID = 1L;
  private List<Paper> model;
  private Controller controller;
  private JTable table;
  private JScrollPane scrollPane;
  private String[] columns = {"Title", "Category", "Acceptance Status"};
  private Author author;
  private TableModel tableModel;
  private List<Paper> data;
  private JButton ViewEdit, DeleteSubmission, AddSubmission, ViewReview1, ViewReview2, ViewReview3;
  private MainView parent;

  public AuthorView(User aUser) {
    author = new Author(aUser);
    model = author.viewPapers();
    controller = new RevisedAuthorViewController(this);
    
    // Configure view/edit button
    ViewEdit = new JButton("View/Edit Details");
    ViewEdit.setEnabled(false);
    ViewEdit.setActionCommand("view_edit");
    ViewEdit.addActionListener(controller);
    
    // Configure view review 1 button
    ViewReview1 = new JButton("View Review 1");
    ViewReview1.setEnabled(false);
    ViewReview1.setActionCommand("view_review1");
    ViewReview1.addActionListener(controller);
    
    // Configure view review 2 button
    ViewReview2 = new JButton("View Review 2");
    ViewReview2.setEnabled(false);
    ViewReview2.setActionCommand("view_review2");
    ViewReview2.addActionListener(controller);
    
    // Configure view review 3 button
    ViewReview3 = new JButton("View Review 3");
    ViewReview3.setEnabled(false);
    ViewReview3.setActionCommand("view_reviews3");
    ViewReview3.addActionListener(controller);
    
    // Configure Delete Submission button
    DeleteSubmission = new JButton("Delete Submission");
    DeleteSubmission.setEnabled(false);
    DeleteSubmission.setActionCommand("delete_submission");
    DeleteSubmission.addActionListener(controller);
    
    // Configure the Add Submission button
    AddSubmission = new JButton("Add Submission");
    // Disables new submission button if deadline is passed
    if(!author.canSubmitOrModify())
    {
    	AddSubmission.setEnabled(false);
    }
    AddSubmission.setActionCommand("add_submission");
    AddSubmission.addActionListener(controller);
   
    tableModel = new TableModel(model);
    table = new JTable(tableModel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowSelectionAllowed(true);
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
      @Override
      public void valueChanged(ListSelectionEvent event) {
          //Enable view/edit button when when selection is made
    	  ViewEdit.setEnabled(true);
    	  // Only enables delete button if selected and deadline is not passed
    	  if(author.canDelete()) {
        	  DeleteSubmission.setEnabled(true);
    	  }
    	  // Only enables view reviews button if selected paper has a final decision.
    	  int selectedRow = table.getSelectedRow();
    	  if(selectedRow >= 0) {
      	      Paper paper = data.get(selectedRow);
      	      if (paper.getStatus().equals("ACCEPT") || paper.getStatus().equals("DECLINE")) 
      	      {
      	    	  ViewReview1.setEnabled(true);
      	    	  ViewReview2.setEnabled(true);
      	    	  ViewReview3.setEnabled(true);
      	      }
    	  }  
      }
    });
    
    // adds scroll panel for papers to main panel
    setLayout(new BorderLayout());
    scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.NORTH);
    
    // adds auxiliary panel for buttons to main panel
    JPanel southPanel = new JPanel();
    southPanel.add(AddSubmission);
    southPanel.add(ViewEdit);
    southPanel.add(DeleteSubmission);
    southPanel.add(ViewReview1);
    southPanel.add(ViewReview2);
    southPanel.add(ViewReview3);
    add(southPanel, BorderLayout.SOUTH);
    
    parent = (MainView) getTopLevelAncestor();
    setVisible(true);
  }
  
  public TableModel getTableModel() {
    return tableModel;
  }
  
  public Paper getSelectedRow() {   
    int selectedRow = table.getSelectedRow();
    
    if(selectedRow >= 0)
      return data.get(selectedRow);
    
    return null;
  }
  
  public Author getAuthor() {
    return author;
  }
  
  public MainView getMainView() {
    return parent;
  }
  
  //:) I know.....
  public void disableButton() {
	ViewEdit.setEnabled(false);
	DeleteSubmission.setEnabled(false);
	AddSubmission.setEnabled(false);
    ViewReview1.setEnabled(false);
    ViewReview2.setEnabled(false);
    ViewReview3.setEnabled(false);
  }
  
  public class TableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private TableModel(List<Paper> model) {
      super();
      
      if(model == null)
        data = new ArrayList<Paper>();
      else
        data = model;
    }
    
    /**
     * Adds row to table
     */
    public void addRow(Paper aObject) {
      data.add(aObject);
      this.fireTableDataChanged();
    }
    
    /**
     * Deletes row from table
     */
    public void deleteRow(Paper aObject) {
      data.remove(aObject);
      this.fireTableDataChanged();
    }
    
    @Override
    public int getColumnCount() {
      return columns.length;
    }

    @Override
    public int getRowCount() {
      return data.size();
    }

    @Override
    public String getColumnName(int columnIndex){
      return columns[columnIndex];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex ) {
      Paper paper = data.get(rowIndex); 
      String result = null;
      switch(columnIndex) {
        case 0: result = paper.getTitle(); break;
        case 1: result = paper.getCategory(); break;
        case 2: result = paper.getAcceptanceStatus().toString(); break;
      }
      
      return result;
    }
    
    @Override
    public Class getColumnClass(int columnIndex) {
      return String.class;
    }
  }
}

