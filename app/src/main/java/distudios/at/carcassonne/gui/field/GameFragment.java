package distudios.at.carcassonne.gui.field;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import distudios.at.carcassonne.CarcassonneApp;
import distudios.at.carcassonne.R;
import distudios.at.carcassonne.engine.logic.CState;
import distudios.at.carcassonne.engine.logic.Card;
import distudios.at.carcassonne.engine.logic.CardDataBase;
import distudios.at.carcassonne.engine.logic.ExtendedCard;
import distudios.at.carcassonne.engine.logic.GameState;
import distudios.at.carcassonne.engine.logic.IGameController;
import distudios.at.carcassonne.engine.logic.Orientation;
import distudios.at.carcassonne.engine.logic.PeepPosition;
import distudios.at.carcassonne.networking.INetworkController;
import distudios.at.carcassonne.networking.connection.CarcassonneMessage;
import distudios.at.carcassonne.networking.connection.PlayerInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment implements PlayfieldView.ICardPlaced , CheatDialog.DialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button buttonEndTurn;
    private Button buttonRotate;
    private Button buttonPeep;
    public ImageButton buttonDrawCard;
    private TextView textViewStatus;
    private TextView textViewStatusPeeps;

    Bitmap source;

    @Override
    public void getBitmapInteger(Integer i) {

        buttonDrawCard.setImageDrawable(new BitmapDrawable(getResources(),PlayfieldView.cardIdToBitmap(i)));
        playfieldView.addPossibleLocations();
        updateFromGameState();
        source=PlayfieldView.cardIdToBitmap(i);

    }

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayfieldView playfieldView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_game, container, false);

        playfieldView = view.findViewById(R.id.view_playfield);
        playfieldView.callbackCardPlaced = this;

        final IGameController controller = CarcassonneApp.getGameController();
        buttonEndTurn = view.findViewById(R.id.button_endTurn);
        buttonEndTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonDrawCard.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
                controller.endTurn();
                updateFromGameState();
            }
        });

        buttonDrawCard = view.findViewById(R.id.button_drawCard);
        buttonDrawCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controller.getCState() != CState.DRAW_CARD) return;
                // open dialog for card drawing

                CheatDialog cheatDialog = new CheatDialog();
                cheatDialog.setTargetFragment(GameFragment.this,1);
                cheatDialog.show(getFragmentManager(),"Choose Card Dialog");

            }
        });






        final GameFragment thisFrag = this;
        buttonPeep = view.findViewById(R.id.button_peep);
        buttonPeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePeepDialog dialog = new PlacePeepDialog();
                dialog.fragment = thisFrag;
                dialog.show(thisFrag.getFragmentManager(), "peep");
            }
        });

        buttonRotate = view.findViewById(R.id.button_rotate);
        buttonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card c = controller.getCurrentCard();
                c.rotate();

                ExtendedCard ec = CardDataBase.getCardById(c.getId());
                //buttonDrawCard.setImageDrawable(new BitmapDrawable(getResources(), PlayfieldView.cardIdToBitmap(ec.getId())));


                Matrix m = new Matrix();
                if (c.getOrientation() == Orientation.NORTH) {
                    m.postRotate(0);
                } else if (c.getOrientation() == Orientation.EAST) {
                    m.postRotate(90);
                } else if (c.getOrientation() == Orientation.SOUTH) {
                    m.postRotate(180);
                } else {
                    m.postRotate(270);
                }
                Bitmap rotated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
                buttonDrawCard.setImageBitmap(rotated);

                playfieldView.addPossibleLocations();
            }
        });
        textViewStatus = view.findViewById(R.id.text_status);
        textViewStatusPeeps = view.findViewById(R.id.text_status_peeps);

        INetworkController nC = CarcassonneApp.getNetworkController();
        PlayerInfo pi = nC.getPlayerInfo(nC.getDevicePlayerNumber());
        textViewStatusPeeps.setTextColor(pi.color);

        updateFromGameState();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updatePlayField() {
        playfieldView.initFieldFromGameState();
    }

    public void updateStatusText() {
        IGameController controller = CarcassonneApp.getGameController();
        CState state = controller.getCState();
        String text = getString(R.string.text_state_waiting);

        switch (state) {
            case DRAW_CARD:
                text  = getString(R.string.text_state_draw_card);
                break;
            case PLACE_CARD:
                text = getString(R.string.text_state_place_card);
                break;
            case PLACE_FIGURE:
                text = getString(R.string.text_state_place_peep);
                break;
            case END_TURN:
                text = getString(R.string.text_end_turn);
                break;
            default:
                int number = controller.getGameState().currentPlayer;
                PlayerInfo info = CarcassonneApp.getNetworkController().getPlayerInfo(number);
                if (info != null) {
                    text += " " + info.deviceName;
                }
        }

        textViewStatus.setText(text);

        int peepsPlaced = CarcassonneApp.getGameController().peepsLeft();
        String peepStatus = "Peeps placed: " + peepsPlaced + ", Peeps left: " + (10-peepsPlaced); // todo remove 10
        textViewStatusPeeps.setText(peepStatus);

    }

    public void updateFromGameState() {
        IGameController controller = CarcassonneApp.getGameController();

        switch (controller.getCState()) {
            case WAITING:
                buttonEndTurn.setEnabled(false);
                buttonPeep.setEnabled(false);
                buttonRotate.setEnabled(false);
                break;
            case DRAW_CARD:
                buttonEndTurn.setEnabled(false);
                buttonPeep.setEnabled(false);
                buttonRotate.setEnabled(false);
                break;
            case PLACE_CARD:
                buttonEndTurn.setEnabled(false);
                buttonPeep.setEnabled(false);
                buttonRotate.setEnabled(true);
                break;
            case PLACE_FIGURE:
                buttonEndTurn.setEnabled(true);
                buttonPeep.setEnabled(CarcassonneApp.getGameController().canPlacePeep());
                buttonRotate.setEnabled(false);
                break;
            case END_TURN:
                buttonEndTurn.setEnabled(true);
                buttonPeep.setEnabled(false);
                buttonRotate.setEnabled(false);
                break;
        }

        updateStatusText();
        playfieldView.initFieldFromGameState();
    }

    @Override
    public void cardPlaced(int x, int y) {
        IGameController controller = CarcassonneApp.getGameController();
        Card c = controller.getCurrentCard();
        c.setxCoordinate(x);
        c.setyCoordinate(y);
        controller.placeCard(c);
        updateFromGameState();
    }
}