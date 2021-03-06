package RestarantApp.aaditionalClass;

import RestarantApp.Billing.ItemSelectedListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.*;

public class AutoCompleteItemTextField extends TextField { /** The existing autocomplete entries. */
private SortedSet<String> entries = null;
    /** The popup used to select an entry. */
    private ContextMenu entriesPopup;
    /** Construct a new AutoCompleteTextField. */
    public static ItemSelectedListener itemSelectedListener;
    public AutoCompleteItemTextField(ItemSelectedListener itemSelectListener)
    {
        this.itemSelectedListener = itemSelectListener;
    }


    public AutoCompleteItemTextField()
    {
        super();
        entries = new TreeSet<>();
        entriesPopup = new ContextMenu();
        textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                System.out.println("entry size--->>"+entries.size());
                if (getText().length() == 0)
                {
                    entriesPopup.hide();
                } else
                {
                    LinkedList<String> searchResult = new LinkedList<>();

                    char ch = getText().charAt(0);
                    String str = getText().trim();
                    if (Character.isUpperCase(ch))
                    {
                        ArrayList<String> arrayList =new ArrayList();
                        ArrayList<String> tempArray =new ArrayList();
                        arrayList.addAll(entries);
                        for (int i= 0 ; i < arrayList.size() ; i ++)
                        {
                            String s1 = arrayList.get(i).toLowerCase();
                            tempArray.add(s1);
                        }
                        str = str.toLowerCase();
                        for (int  i= 0 ; i < tempArray.size() ; i++)
                        {
                            String  s1 = tempArray.get(i);
                            String actualValue = arrayList.get(i);
                            if (s1.contains(str))
                            {
                                searchResult.add(actualValue);
                            }
                        }
                        /*searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
                        searchResult.addAll(entries.subSet(getText().toLowerCase(), getText().toLowerCase() + Character.MAX_VALUE));*/

                    }else if (Character.isLowerCase(ch))
                    {
                        ArrayList<String> arrayList =new ArrayList();
                        ArrayList<String> tempArray =new ArrayList();
                        arrayList.addAll(entries);
                        str = str.toUpperCase();
                        for (int i= 0 ; i < arrayList.size() ; i ++)
                        {
                            String s1 = arrayList.get(i).toLowerCase();
                            tempArray.add(s1);
                        }
                        String str1 = str.toLowerCase();
                        for (int  i= 0 ; i < tempArray.size() ; i++)
                        {
                            String  s1 = tempArray.get(i);
                            String actualValue = arrayList.get(i);
                            if (s1.contains(str))
                            {
                                searchResult.add(actualValue);
                            }
                            if (s1.contains(str1))
                            {
                                searchResult.add(actualValue);
                            }
                        }

                        /*searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
                        searchResult.addAll(entries.subSet(getText().toUpperCase(), getText().toUpperCase() + Character.MAX_VALUE));*/
                    }else if (Character.isDigit(ch))
                    {
                        searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));

                    }

                    if (entries.size() > 0)
                    {
                        populatePopup(searchResult);
                        if (!entriesPopup.isShowing())
                        {
                            entriesPopup.show(AutoCompleteItemTextField.this, Side.BOTTOM, 0, 0);
                        }
                    } else
                    {
                        entriesPopup.hide();
                    }
                }
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                entriesPopup.hide();
            }
        });
    }

    public void hidePopUp()
    {
        entriesPopup.hide();
    }
    /**
     * Get the existing set of autocomplete entries.
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() {

        return entries;
    }

    /**
     * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++)
        {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent) {
                    setText(result);
                    itemSelectedListener.getSelectedResult(result);
                    entriesPopup.hide();
                }
            });
            menuItems.add(item);

        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }





    /**
     * Build TextFlow with selected text. Return "case" dependent.
     *
     * @param text - string with text
     * @param filter - string to select in text
     * @return - TextFlow
     */
    public static TextFlow buildTextFlow(String text, String filter) {
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        Text textBefore = new Text(text.substring(0, filterIndex));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        Text textFilter = new Text(text.substring(filterIndex,  filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.ORANGE);
        textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        return new TextFlow(textBefore, textFilter, textAfter);
    }


}
